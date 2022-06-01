package com.kineteco;

import com.kineteco.model.ManufactureOrder;
import com.kineteco.model.Product;
import com.kineteco.model.ProductInventory;
import io.quarkus.logging.Log;
import io.smallrye.reactive.messaging.kafka.Record;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;
import java.time.Duration;
import java.util.List;
import java.util.stream.Collectors;

@ApplicationScoped
public class ManufactureOrderEmitter {

   @Channel("orders")
   Emitter<Record<String, ManufactureOrder>> emitter;

   @Channel("products")
   Emitter<Record<String, Product>> productsEmitter;

   public void sendManufactureOrder(String sku, int quantity) {
      Log.debugf("Emit manufacturing message for sku %s with %d quantity", sku, quantity);
      ManufactureOrder manufactureOrder = new ManufactureOrder();
      manufactureOrder.sku = sku;
      manufactureOrder.quantity = quantity;
      emitter.send(Record.of(sku, manufactureOrder));
   }

   public void sendProducts() {
      Log.info("Send products");
      List<Record<String, Product>> records = ProductInventory.<ProductInventory>findAll().list().await()
            .atMost(Duration.ofMinutes(1)).stream().map(p -> Record.of(p.sku, p.toProduct()))
            .collect(Collectors.toList());
      for (Record<String, Product> r: records) {
         productsEmitter.send(r);
      }
   }
}
