package com.kineteco;

import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@CommandLine.Command(name = "view", description = "View customer data", mixinStandardHelpOptions = true)
public class ViewCustomerCommand implements Runnable {
   Long customerId;

   CustomerService customerService;

   public ViewCustomerCommand(CustomerService customerService) {
      this.customerService = customerService;
   }

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id", defaultValue = "1")
   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
   }

   @Override
   public void run() {
      customerService.displayCustomer(customerId);
   }
}

