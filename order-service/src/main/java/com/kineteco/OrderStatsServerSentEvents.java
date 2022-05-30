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

   @GET
   public Multi<Collection<String>> stream() {
      return null;
   }
}
