package com.kineteco;

import com.kineteco.model.ManufactureOrder;
import io.quarkus.logging.Log;
import org.eclipse.microprofile.reactive.messaging.Channel;
import org.eclipse.microprofile.reactive.messaging.Emitter;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class ManufactureOrderEmitter {
   @Channel("orders")
   Emitter<ManufactureOrder> emitter;

   public void sendManufactureOrder(String sku, int quantity) {
      Log.debugf("Emit manufacturing message for sku %s with %d quantity", sku, quantity);
      ManufactureOrder manufactureOrder = new ManufactureOrder();
      manufactureOrder.sku = sku;
      manufactureOrder.quantity = quantity;
      for (int i = 0; i < 200; i++){
         emitter.send(manufactureOrder);
      }
   }
}
