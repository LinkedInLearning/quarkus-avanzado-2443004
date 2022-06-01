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
