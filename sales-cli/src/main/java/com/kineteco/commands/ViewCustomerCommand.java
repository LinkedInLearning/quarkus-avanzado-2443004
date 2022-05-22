package com.kineteco.commands;

import com.kineteco.service.CustomerService;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
@CommandLine.Command(name = "view", description = "View customer data", mixinStandardHelpOptions = true)
public class ViewCustomerCommand implements Runnable {
   String customerId;

   CustomerService customerService;

   public ViewCustomerCommand(CustomerService customerService) {
      this.customerService = customerService;
   }

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id", defaultValue = "1")
   public void setCustomerId(String customerId) {
      this.customerId = customerId;
   }

   @Override
   public void run() {
      customerService.displayCustomer(customerId);
   }
}

