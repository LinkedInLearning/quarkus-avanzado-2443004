package com.kineteco;

import picocli.CommandLine;

@CommandLine.Command
public class ViewCustomerCommand implements Runnable {

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id", defaultValue = "1")
   Long customerId;

   private final CustomerService customerService;

   public ViewCustomerCommand(CustomerService customerService) {
      this.customerService = customerService;
   }

   @Override
   public void run() {
      customerService.displayCustomer(customerId);
   }
}

