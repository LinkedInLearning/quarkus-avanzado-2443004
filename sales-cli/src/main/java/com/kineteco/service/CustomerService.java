package com.kineteco.service;

import com.kineteco.model.Customer;

import javax.enterprise.context.ApplicationScoped;
import javax.transaction.Transactional;
import java.util.Optional;

@ApplicationScoped
public class CustomerService {
   @Transactional
   public void displayCustomer(Long id) {
      System.out.println(Customer.findByIdOptional(id));
   }

   @Transactional
   public void updateEmail(Long id, String email) {
      Optional<Customer> customerOpt = Customer.findByIdOptional(id);
      if (customerOpt.isEmpty()) {
         System.out.println("Unable to find customer");
         return;
      }

      Customer customer = customerOpt.get();
      customer.email = email;
      customer.persist();
      System.out.println("Customer e-mail modified");
      System.out.println(Customer.findByIdOptional(id));
   }
}
