package com.kineteco.rest;

import com.kineteco.client.Product;
import com.kineteco.fallbacks.SalesServiceFallbackHandler;
import com.kineteco.model.CustomerSale;
import com.kineteco.service.MetricsService;
import com.kineteco.service.ProductInventoryService;
import com.kineteco.service.SalesService;
import io.micrometer.core.annotation.Timed;
import org.eclipse.microprofile.faulttolerance.Bulkhead;
import org.eclipse.microprofile.faulttolerance.CircuitBreaker;
import org.eclipse.microprofile.faulttolerance.Fallback;
import org.eclipse.microprofile.faulttolerance.Retry;
import org.eclipse.microprofile.faulttolerance.Timeout;
import org.eclipse.microprofile.faulttolerance.exceptions.TimeoutException;
import org.jboss.logging.Logger;
import org.jboss.resteasy.api.validation.ResteasyViolationException;

import javax.inject.Inject;
import javax.transaction.Transactional;
import javax.validation.Valid;
import javax.ws.rs.BadRequestException;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Path("/sales")
public class SalesResource {

    private static final Logger LOGGER = Logger.getLogger(SalesResource.class);

    @Inject
    SalesService salesService;

    @Inject
    ProductInventoryService productInventoryService;

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("health")
    public String health() {
        return "Sales Service is up!!";
    }

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<CustomerSale> sales() {
        return CustomerSale.listAll();
    }

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{sku}/availability")
    @Timeout(value = 100)
    @Retry(retryOn = TimeoutException.class, delay = 100, jitter = 25)
    @Fallback(value = SalesServiceFallbackHandler.class)
    public Response available(@PathParam("sku") String sku, @QueryParam("units") Integer units) {
        LOGGER.debugf("available %s %d", sku, units);
        if (units == null) {
            throw new BadRequestException("units query parameter is mandatory");
        }


        return Response.ok(productInventoryService.getStock(sku) >= units).build();
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    @Timeout(value = 200)
    @CircuitBreaker(
          requestVolumeThreshold=3,
          failureRatio = 0.66,
          delay = 1,
          delayUnit = ChronoUnit.SECONDS
    )
    @Fallback(value = SalesServiceFallbackHandler.class, skipOn = ResteasyViolationException.class)
    @Bulkhead(value= 1)
    @Transactional
    public Response createDeluxeCommand(@Valid CustomerCommand command) {
        Product product = productInventoryService.findProduct(command.getSku());
        if ("DELUXE".equals(product.getProductLine())) {
            CustomerSale customerSale = salesService.createCustomerSale(command, product);
            return Response.ok(customerSale).build();
        }

        return Response.status(Response.Status.BAD_REQUEST).build();
    }

}