# Leer y fusionar dos streams con Kafka Streams y Quarkus

* Dependencia
```xml
 <dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-kafka-streams</artifactId>
</dependency>
```

* Product Inventory
```java
@Channel("orders")
Emitter<Record<String, ManufactureOrder>> emitter;

@Channel("products")
Emitter<Record<String, Product>> productsEmitter;
```


* Order Stats
```properties
## Kafka Streams
quarkus.kafka-streams.application-id=order-stats-service
quarkus.kafka-streams.topics=orders,products

# streams options
kafka-streams.cache.max.bytes.buffering=10240
kafka-streams.commit.interval.ms=1000
kafka-streams.metadata.max.age.ms=500
kafka-streams.auto.offset.reset=earliest
kafka-streams.metrics.recording.level=DEBUG
kafka-streams.consumer.heartbeat.interval.ms=200
```


* Topology Producer
```java
package com.kineteco.streams;

import com.kineteco.model.ManufactureOrder;
import com.kineteco.model.Product;
import com.kineteco.model.ProductManufatureOrder;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
import io.quarkus.logging.Log;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.Topology;
import org.apache.kafka.streams.kstream.Consumed;
import org.apache.kafka.streams.kstream.GlobalKTable;
import org.apache.kafka.streams.kstream.Materialized;
import org.apache.kafka.streams.kstream.Produced;
import org.apache.kafka.streams.state.KeyValueBytesStoreSupplier;
import org.apache.kafka.streams.state.Stores;

import javax.enterprise.context.ApplicationScoped;
import javax.enterprise.inject.Produces;

@ApplicationScoped
public class TopologyProducer {

   public static final String PRODUCTS_TOPIC = "products";
   public static final String ORDERS_TOPIC = "orders";
   public static final String STATS_TOPIC = "stats";

   @Produces
   public Topology buildTopology() {
      StreamsBuilder builder = new StreamsBuilder();

      ObjectMapperSerde<ManufactureOrder> ordersSerde = new ObjectMapperSerde<>(ManufactureOrder.class);
      ObjectMapperSerde<Product> productsSerde = new ObjectMapperSerde<>(Product.class);
      ObjectMapperSerde<ProductManufatureOrder> pmoSerde = new ObjectMapperSerde<>(ProductManufatureOrder.class);

      GlobalKTable<String, Product> products = builder.globalTable(PRODUCTS_TOPIC,
            Consumed.with(Serdes.String(), productsSerde));

      builder.stream(ORDERS_TOPIC,
                  Consumed.with(Serdes.String(), ordersSerde))
            .join(
                  products,
                  (sku, order) -> sku,
                  (order, product) -> {
                     ProductManufatureOrder productManufatureOrder = new ProductManufatureOrder();
                     productManufatureOrder.sku = product.sku;
                     productManufatureOrder.name = product.name;
                     productManufatureOrder.category = product.category;
                     productManufatureOrder.unitsAvailable = product.unitsAvailable;
                     productManufatureOrder.quantity = order.quantity;
                     Log.info(productManufatureOrder);
                     return productManufatureOrder;
                  })
            .to(STATS_TOPIC,
                  Produced.with(Serdes.String(), pmoSerde));

      return builder.build();
   }
}

```