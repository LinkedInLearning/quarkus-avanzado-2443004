package com.kineteco;

import com.kineteco.service.CustomerService;
import picocli.CommandLine;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@CommandLine.Command(name = "view",
      description = "view customer",
      mixinStandardHelpOptions = true,
      version = "1.0"
)
@ApplicationScoped
public class ViewCustomerCommand implements Runnable {
   @Inject
   CustomerService customerService;

   Long customerId;

   @CommandLine.Option(names = {"--id", "-i"}, description = "customer id")
   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
   }

   @Override
   public void run() {
      customerService.displayCustomer(customerId);
   }
}

