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
