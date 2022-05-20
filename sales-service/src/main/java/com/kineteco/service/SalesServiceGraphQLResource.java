package com.kineteco.service;

import com.kineteco.model.CustomerSale;
import com.kineteco.model.ProductSale;
import org.eclipse.microprofile.graphql.Description;
import org.eclipse.microprofile.graphql.GraphQLApi;
import org.eclipse.microprofile.graphql.Query;

import java.util.List;

@GraphQLApi
public class SalesServiceGraphQLResource {

   @Query("allCustomerSales")
   @Description("Get all product sales")
   public List<CustomerSale> getAllCustomerSales() {
      return CustomerSale.listAll();
   }
}
