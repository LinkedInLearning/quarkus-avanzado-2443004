package com.kineteco.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.OrderServiceWebsocket;
import com.kineteco.model.ManufactureOrder;
import com.kineteco.model.OrderStat;
import io.smallrye.mutiny.Multi;
import io.vertx.core.impl.ConcurrentHashSet;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;


@ApplicationScoped
public class StatsService {
   private static final Logger LOGGER = Logger.getLogger(OrderServiceWebsocket.class);

   @Inject
   ObjectMapper mapper;

   private final Collection<OrderStat> stats = new ConcurrentHashSet<>();

   @Incoming("orders")
   @Outgoing("orders-stats")
   public Multi<Collection<String>> computeTopProducts(Multi<ManufactureOrder> orders) {
      return orders
            .group().by(order -> order.sku)
            .onItem().transformToMultiAndMerge(group ->
                  group
                        .onItem().scan(OrderStat::new, this::incrementOrderCount))
            .select().where(order -> order.sku != null && order.sku.startsWith("KE1"))
            .onItem().transform(this::onNewStat)
            .invoke(() -> LOGGER.info("Order received. Computed the top product stats"));
   }

   private Collection<String> onNewStat(OrderStat score) {
      stats.add(score);
      List<String> statsResult =
            stats.stream()
                  .sorted(Comparator.comparingInt(s -> -1 * s.count))
                  .map(this::transformJson)
                  .collect(Collectors.toList());
      return statsResult;
   }

   private OrderStat incrementOrderCount(OrderStat stat, ManufactureOrder manufactureOrder) {
      stat.sku = manufactureOrder.sku;
      stat.count = stat.count + 1;
      return stat;
   }

   public String transformJson(OrderStat score) {
      try {
         return mapper.writeValueAsString(score);
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }
   }
}
