package com.kineteco.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.model.ManufactureOrder;
import com.kineteco.model.OrderStat;
import io.smallrye.mutiny.Multi;
import io.vertx.core.impl.ConcurrentHashSet;
import org.eclipse.microprofile.reactive.messaging.Incoming;
import org.eclipse.microprofile.reactive.messaging.Outgoing;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@ApplicationScoped
public class StatsService {
   private static final Logger LOGGER = Logger.getLogger(StatsService.class);

   @Incoming("orders")
   @Outgoing("orders-stats")
   public Multi<Collection<String>> computeTopProducts(Multi<ManufactureOrder> orders) {
      return orders
            .select().where(order -> order.sku != null && order.sku.startsWith("KE1"))
            .group().by(order -> order.sku)
            .onItem().transformToMultiAndMerge(g ->
                  g.onItem().scan(OrderStat::new, this::incrementOrderCount))
            .onItem().transform(this::onNewStat)
            .invoke(() -> LOGGER.info("Order received. Computed the top product stats"));
   }

   private final Map<String, OrderStat> stats = new HashMap<>();

   private Collection<String> onNewStat(OrderStat stat) {
      if (stat.sku != null) {
         stats.put(stat.sku, stat);
      }

      return stats.values()
            .stream()
            .sorted(Comparator.comparingInt(s -> -1 * s.count))
            .map(this::transformJson)
            .collect(Collectors.toUnmodifiableList());
   }

   @Inject
   ObjectMapper mapper;

   public String transformJson(OrderStat score) {
      try {
         return mapper.writeValueAsString(score);
      } catch (JsonProcessingException e) {
         throw new RuntimeException(e);
      }
   }

   private OrderStat incrementOrderCount(OrderStat stat, ManufactureOrder manufactureOrder) {
      stat.sku = manufactureOrder.sku;
      stat.count = stat.count + 1;
      return stat;
   }
}
