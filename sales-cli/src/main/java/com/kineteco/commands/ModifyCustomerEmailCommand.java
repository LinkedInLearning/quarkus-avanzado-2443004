package com.kineteco.commands;

import com.kineteco.service.CustomerService;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
@CommandLine.Command(name = "update", description = "Update customer data")
public class ModifyCustomerEmailCommand implements Runnable {

   String id;
   String email;

   @Inject
   CustomerService customerService;

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id")
   public void setCustomerId(String id) {
      this.id = id;
   }

   @CommandLine.Option(names = {"-e", "--email"}, description = "Customer email")
   public void setEmail(String email) {
      this.email = email;
   }

   @Override
   public void run() {
      customerService.updateEmail(id, email);
   }
}