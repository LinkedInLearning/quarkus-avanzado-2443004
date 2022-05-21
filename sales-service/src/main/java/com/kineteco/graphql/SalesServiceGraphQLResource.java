package com.kineteco.graphql;

import com.kineteco.model.Customer;
import com.kineteco.model.CustomerSale;
import com.kineteco.model.ProductSale;
import io.smallrye.mutiny.Uni;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Name;
import org.eclipse.microprofile.graphql.Query;
import org.eclipse.microprofile.graphql.Source;

import java.util.List;
import java.util.stream.Collectors;

@GraphQLApi
public class SalesServiceGraphQLResource {

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

   @Query("getCustomerReactive")
   @Description("Get a Customer Reactive")
   public Uni<Customer> getCustomerReactive(@Name("customerId") String customerId) {
      return Uni.createFrom().item(Customer.findByCustomerId(customerId));
   }

   // Extend the getCustomerAPI
   public List<ProductSale> productSales(@Source Customer customer) {
       return CustomerSale.find("customer.customerId", customer.customerId)
            .<CustomerSale>list().stream()
            .map(cs -> cs.productSale)
            .flatMap(List::stream)
            .collect(Collectors.toList());
   }
}
