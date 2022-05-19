package com.kineteco;

import com.kineteco.model.Customer;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/customers")
public class CustomerResource {

    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> customers() {
        return Customer.listAll();
    }

    @GET
    @Path("/display/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public String get(@PathParam("id") Long id) {
        Optional<Customer> customer = Customer.<Customer>findByIdOptional(id);
        String name = customer.map(c -> c.name).orElse("Unknown");
        return name;
    }
}