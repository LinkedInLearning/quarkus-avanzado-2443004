# Agregar y realizar query inteactivas de streams de datos con Kafka Streams en Quarkus

* Dependencia
```xml
 <dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-kafka-streams</artifactId>
</dependency>
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
* Resource
```java
package com.kineteco;

import com.kineteco.model.ProductManufatureOrderStats;
import com.kineteco.streams.TopologyProducer;
import org.apache.kafka.streams.KafkaStreams;
import org.apache.kafka.streams.StoreQueryParameters;
import org.apache.kafka.streams.errors.InvalidStateStoreException;
import org.apache.kafka.streams.state.QueryableStoreTypes;
import org.apache.kafka.streams.state.ReadOnlyKeyValueStore;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("order-stats")
public class OrderStatsResource {

   @Inject
   KafkaStreams streams;

   @GET
   @Path("/{sku}")
   public Response getStats(@PathParam("sku") String sku) {
      ReadOnlyKeyValueStore<String, ProductManufatureOrderStats> productManufatureOrderStats = getProductStore();
      if (productManufatureOrderStats == null) {
         return Response.ok("No products").build();
      }

      return Response.ok(productManufatureOrderStats.get(sku)).build();
   }

   private ReadOnlyKeyValueStore<String, ProductManufatureOrderStats> getProductStore() {
         try {
            return streams.store(
                  StoreQueryParameters.fromNameAndType(TopologyProducer.PRODUCTS_STORE,
                        QueryableStoreTypes.keyValueStore()));
         } catch (InvalidStateStoreException e) {
            // ignore, store not ready yet
            return null;
         }
   }
}

```

* Topology Producer
```java
package com.kineteco.streams;

import com.kineteco.model.ManufactureOrder;
import com.kineteco.model.Product;
import com.kineteco.model.ProductManufatureOrder;
import com.kineteco.model.ProductManufatureOrderStats;
import io.quarkus.kafka.client.serialization.ObjectMapperSerde;
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

   public static final String PRODUCTS_STORE = "products-store";
   public static final String PRODUCTS_TOPIC = "products";
   public static final String ORDERS_TOPIC = "orders";
   public static final String STATS_TOPIC = "stats";

   @Produces
    public Topology buildTopology() {
        StreamsBuilder builder = new StreamsBuilder();

        ObjectMapperSerde<ManufactureOrder> ordersSerde = new ObjectMapperSerde<>(ManufactureOrder.class);
        ObjectMapperSerde<Product> productsSerde = new ObjectMapperSerde<>(Product.class);
        ObjectMapperSerde<ProductManufatureOrderStats> pmoStatsSerde = new ObjectMapperSerde<>(ProductManufatureOrderStats.class);

        KeyValueBytesStoreSupplier productsStore = Stores.persistentKeyValueStore(PRODUCTS_STORE);

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
                            productManufatureOrder.unitsAvailable = productManufatureOrder.unitsAvailable;
                            productManufatureOrder.quantity = order.quantity;
                            return productManufatureOrder;
                        })
                .groupByKey()
                .aggregate(
                        ProductManufatureOrderStats::new,
                        (sku, productManufatureOrder, pmoStats) -> pmoStats.updateFrom(productManufatureOrder),
                        Materialized.<String, ProductManufatureOrderStats> as(productsStore)
                                .withKeySerde(Serdes.String())
                                .withValueSerde(pmoStatsSerde))
                .toStream()
                .to(STATS_TOPIC,
                        Produced.with(Serdes.String(), pmoStatsSerde));

        return builder.build();
    }
}

```

* Product Inventory
```java
@Channel("orders")
Emitter<Record<String, ManufactureOrder>> emitter;

@Channel("products")
Emitter<Record<String, Product>> productsEmitter;
```
