package com.kineteco;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

@Path("order-stats")
public class OrderStatsResource {

   @GET
   @Path("/{sku}")
   public Response getStats(@PathParam("sku") String sku) {

      return Response.ok("TODO").build();
   }
}
