package com.kineteco;

import com.kineteco.model.Customer;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/customers")
public class CustomerResource {

    private static final Logger LOGGER = Logger.getLogger(CustomerResource.class);

//    @Inject
//    Template customers;

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> customers() {
        return Customer.listAll();
    }

    @GET
    @Path("/display/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance get(@PathParam("id") Long id) {
        Optional<Customer> customer = Customer.<Customer>findByIdOptional(id);
        String name = customer.map(c -> c.name).orElse("Unknown");
        String email = customer.map(c -> c.email).orElse("Unknown");
//        return customers.data("name", name);
        return Templates.customers(name, email);
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance customers(String name, String email);
    }

}