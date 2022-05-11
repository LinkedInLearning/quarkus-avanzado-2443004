package com.kineteco.service;

import com.kineteco.CustomerCommand;
import com.kineteco.client.Product;
import com.kineteco.model.Customer;
import com.kineteco.model.CustomerSale;
import com.kineteco.model.ProductSale;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.util.ArrayList;

@ApplicationScoped
public class SalesService {

   @Transactional
   public CustomerSale createCustomerSale(CustomerCommand command, Product product) {
      Customer customer = Customer.findByCustomerId(command.getCustomerId());
      CustomerSale customerSale = new CustomerSale();
      customerSale.customer = customer;
      customerSale.productSale = new ArrayList<>();
      ProductSale productSale = new ProductSale();
      productSale.sku = product.getSku();
      productSale.name = product.getName();
      productSale.total = product.getPrice().multiply(new BigDecimal(command.getUnits()));;
      customerSale.productSale.add(new ProductSale());
      customerSale.persist();
      return customerSale;
   }
}
