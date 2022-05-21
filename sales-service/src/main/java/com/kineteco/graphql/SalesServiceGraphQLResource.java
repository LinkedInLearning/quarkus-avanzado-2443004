package com.kineteco.graphql;

import com.kineteco.model.CustomerSale;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
public class SalesServiceGraphQLResource {

   @Query("allCustomerSales")
   @Description("Get all product sales")
   public List<CustomerSale> sales() {
      return CustomerSale.listAll();
   }

}
