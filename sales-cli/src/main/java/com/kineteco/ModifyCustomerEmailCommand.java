package com.kineteco;

import picocli.CommandLine;

@CommandLine.Command
public class ModifyCustomerEmailCommand implements Runnable {

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id")
   Long customerId;

   @CommandLine.Option(names = {"-e", "--email"}, description = "Customer email")
   String email;

   private final CustomerService customerService;

   public ModifyCustomerEmailCommand(CustomerService customerService) {
      this.customerService = customerService;
   }

   @Override
   public void run() {
      customerService.updateEmail(customerId, email);
   }
}

