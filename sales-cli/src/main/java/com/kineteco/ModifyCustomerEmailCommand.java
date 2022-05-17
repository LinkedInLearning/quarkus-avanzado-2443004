package com.kineteco;

import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@CommandLine.Command(name = "update", description = "Update customer data")
public class ModifyCustomerEmailCommand implements Runnable {

   Long customerId;
   String email;

   @Inject
   CustomerService customerService;

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id")
   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
   }

   @CommandLine.Option(names = {"-e", "--email"}, description = "Customer email")
   public void setEmail(String email) {
      this.email = email;
   }

   @Override
   public void run() {
      customerService.updateEmail(customerId, email);
   }
}

