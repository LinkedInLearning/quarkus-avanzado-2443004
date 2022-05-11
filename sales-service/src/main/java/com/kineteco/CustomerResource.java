package com.kineteco;

import com.kineteco.model.Customer;
import org.jboss.logging.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

@Path("/customers")
public class CustomerResource {

    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class);

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> customers() {
        return Customer.listAll();
    }

}