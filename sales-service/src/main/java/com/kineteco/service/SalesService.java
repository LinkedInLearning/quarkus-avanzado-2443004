package com.kineteco.service;

import com.kineteco.client.Product;
import com.kineteco.graphql.CustomerNotFoundException;
import com.kineteco.model.Customer;
import com.kineteco.model.CustomerSale;
import com.kineteco.model.ProductSale;
import com.kineteco.rest.CustomerCommand;

import javax.enterprise.context.ApplicationScoped;
import java.math.BigDecimal;
import java.util.ArrayList;

@ApplicationScoped
public class SalesService {

   public CustomerSale createCustomerSale(CustomerCommand command, Product product) {
      Customer customer = Customer.findByCustomerId(command.getCustomerId())
            .orElseThrow(() -> new CustomerNotFoundException());
      CustomerSale customerSale = new CustomerSale();
      customerSale.customer = customer;
      customerSale.productSale = new ArrayList<>();
      customerSale.persist();

      ProductSale productSale = new ProductSale();
      productSale.sku = product.getSku();
      productSale.name = product.getName();
      productSale.total = product.getPrice().multiply(new BigDecimal(command.getUnits()));
      productSale.persist();

      customerSale.productSale.add(productSale);
      customerSale.persist();
      return customerSale;
   }
}
