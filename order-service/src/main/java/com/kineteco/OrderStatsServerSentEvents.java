package com.kineteco;

import com.kineteco.model.OrderStat;
import io.smallrye.mutiny.Multi;
import org.eclipse.microprofile.reactive.messaging.Channel;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.Collection;

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
