package com.kineteco.service;

import com.kineteco.client.CustomerClient;
import com.kineteco.model.Customer;
import io.smallrye.graphql.client.GraphQLClient;
import io.smallrye.graphql.client.Response;
import io.smallrye.graphql.client.core.Argument;
import io.smallrye.graphql.client.core.Document;
import io.smallrye.graphql.client.dynamic.api.DynamicGraphQLClient;
import io.smallrye.graphql.client.typesafe.api.ErrorOr;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

import java.util.List;
import java.util.concurrent.ExecutionException;

import static io.smallrye.graphql.client.core.Argument.arg;
import static io.smallrye.graphql.client.core.Document.document;
import static io.smallrye.graphql.client.core.Field.field;
import static io.smallrye.graphql.client.core.Operation.operation;

@ApplicationScoped
public class CustomerService {

   @Inject
   CustomerClient customerClient;

   @Inject
   @GraphQLClient("customers")
   DynamicGraphQLClient dynamicCustomerClient;

   public void displayCustomer(String customerId) {
//      System.out.println(customerClient.customerDetail(customerId));
      Document query = document(
            operation(
                  field("getCustomer",
                        List.of(arg("customerId", customerId)),
                        field("id"),
                        field("name"),
                        field("email"),
                        field("customerId"))
            )
      );
      try {
         Response response = dynamicCustomerClient.executeSync(query);
         System.out.println(response);
         System.out.println(response.getObject(Customer.class, "getCustomer"));
      } catch (Exception e) {
         throw new RuntimeException(e);
      }
   }

   public void updateEmail(String id, String email) {
      ErrorOr<Customer> customerErrorOr = customerClient.customerDetail(id);
      if (customerErrorOr.hasErrors()) {
         System.out.println(customerErrorOr.getErrors());
         return;
      }
      Customer customer = customerErrorOr.get();
      customer.email = email;
      Customer updated = customerClient.updateEmail(customer);
      System.out.println("Email updated " + updated);
   }
}
