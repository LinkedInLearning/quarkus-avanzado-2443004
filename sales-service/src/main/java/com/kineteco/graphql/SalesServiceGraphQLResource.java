package com.kineteco.graphql;

import com.kineteco.model.Customer;
import com.kineteco.model.CustomerSale;
import com.kineteco.model.ProductSale;
import io.smallrye.graphql.api.Subscription;
import io.smallrye.mutiny.Multi;
import io.smallrye.mutiny.operators.multi.processors.BroadcastProcessor;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Mutation;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@GraphQLApi
@ApplicationScoped
public class SalesServiceGraphQLResource {

   BroadcastProcessor<Customer> processor = BroadcastProcessor.create();

   @Query("allCustomerSales")
   @Description("Get all product sales")
   public List<CustomerSale> sales() {
      return CustomerSale.listAll();
   }

   @Query("getCustomer")
   @Description("Get a Customer")
   public Customer getCustomer(@Name("customerId") String customerId) {
      return Customer.findByCustomerId(customerId);
   }

   public List<ProductSale> productSales(@Source Customer customer) {
      return CustomerSale.find("customer.customerId", customer.customerId)
            .<CustomerSale>list().stream()
            .map(cs -> cs.productSale)
            .flatMap(List::stream)
            .collect(Collectors.toList());
   }

   @Mutation
   @Transactional
   public Customer createCustomer(Customer customer) {
      if (customer.id == null) {
         customer.persist();
         processor.onNext(customer);
      } else {
         Customer existing = Customer.findById(customer.id);
         existing.name = customer.name;
         existing.email = customer.email;
      }
      return customer;
   }

   @Mutation
   @Transactional
   public boolean deleteCustomer(@Name("id") Long id) {
      return Customer.deleteById(id);
   }

   @Subscription
   public Multi<Customer> customerCreation(){
      return processor;
   }
}
