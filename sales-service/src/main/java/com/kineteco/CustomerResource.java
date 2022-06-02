package com.kineteco;

import com.kineteco.model.Customer;
import io.quarkus.logging.Log;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Template;
import io.quarkus.qute.TemplateInstance;
import org.jboss.logging.Logger;

import javax.inject.Inject;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;
import java.util.Optional;

@Path("/customers")
public class CustomerResource {

    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public List<Customer> customers() {
        Log.debugf("Call get customers");
        return Customer.listAll();
    }

    @GET
    @Path("/display/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance displayCustomer(@PathParam("id") Long id) {
        Optional<Customer> customer = Customer.findByIdOptional(id);
        String name = customer.map(c -> c.name).orElse("Unknown");
        return Templates.customers(customer.get());
    }

    @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance customers(Customer customer);
    }
}