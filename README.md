# Composición y transformación de Streams con Mutiny y Quarkus

Te invito a que repases el vídeo de Quarkus Esencial sobre la programación reactiva.

* Creamos un Server Sent Event Endpoint
```java
@Path("/order-stats")
public class OrderStatsServerSentEvents {
   @Channel("orders-stats")
   Multi<OrderStat> ordersStats;

   @GET
   @Produces(MediaType.SERVER_SENT_EVENTS)
   public Multi<OrderStat> stream() {
      return ordersStats;
   }
}
```

* Stats Service
```java
public class StatsService {
   private static final Logger LOGGER = Logger.getLogger(OrderServiceWebsocket.class);

   @Incoming("orders")
   @Outgoing("orders-stats")
   public Multi<OrderStat> computeTopProducts(Multi<ManufactureOrder> orders) {
      return orders
            .onItem().transform(order -> {
               OrderStat stat = new OrderStat();
               stat.sku = order.sku;
               stat.count = 1;
               return stat;
            })
            .invoke(() -> LOGGER.info("Order received. Computed the top product stats"));
   }
}
```

* Arrancamos vemos un error. Tenemos que configurar broadcast
```properties
mp.messaging.incoming.orders.broadcast=true
```

* Si vamos a la pantalla y lanzamos unos mensajes vemos. Queremos agrupar los count.

```java
 public Multi<OrderStat> computeTopProducts(Multi<ManufactureOrder> orders) {
      return orders
      .group().by(order -> order.sku)
      .onItem().transformToMultiAndMerge(group ->
      group
      .onItem().scan(OrderStat::new, this::incrementOrderCount))
      .onItem().transform(topProducts::onNewStat)
      .invoke(() -> LOGGER.info("Order received. Computed the top product stats"));
      }

private OrderStat incrementOrderCount(OrderStat stat, ManufactureOrder manufactureOrder) {
      stat.sku = manufactureOrder.sku;
      stat.count = stat.count + 1;
      return stat;
      }

@Path("/order-stats")
public class OrderStatsServerSentEvents {
   @Channel("orders-stats")
   Multi<Collection<String>> ordersStats;

   @GET
   @Produces(MediaType.SERVER_SENT_EVENTS)
   public Multi<Collection<String>> stream() {
      return ordersStats;
   }
}
```

* HTML change
```json
 function updateManufactureOrderStats(orderStats) {
      $("#orders-stats").children("p").remove();
      JSON.parse(orderStats).forEach(function(stat) {
      var jsonStat = JSON.parse(stat);
      if (jsonStat.sku == 'null') {
      return;
    }
      $("#orders-stats").append($("<p>" + jsonStat.sku + " [" + jsonStat.count + "]</p>"))
    });
}
```
* Añadir select
```json
.select().where(order -> order.sku != null && order.sku.startsWith("KE1"))
```

* Final
```java
package com.kineteco.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.kineteco.model.OrderStat;
import io.vertx.core.impl.ConcurrentHashSet;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.util.Collection;


@ApplicationScoped
public class StatsService {
   private static final Logger LOGGER = Logger.getLogger(StatsService.class);

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

```