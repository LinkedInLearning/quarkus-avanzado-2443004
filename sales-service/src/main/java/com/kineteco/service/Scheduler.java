package com.kineteco.service;

import com.kineteco.model.Customer;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class Scheduler {
   private static final Logger LOGGER = Logger.getLogger(Scheduler.class);

   public void displayCustomerCount() {
     LOGGER.info(Customer.count());
   }
}
