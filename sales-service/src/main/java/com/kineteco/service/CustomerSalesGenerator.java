package com.kineteco.service;

import com.kineteco.model.Customer;
import com.kineteco.model.CustomerSale;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.runtime.configuration.ProfileManager;
import io.quarkus.scheduler.Scheduled;
import io.quarkus.scheduler.ScheduledExecution;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import javax.inject.Singleton;

@ApplicationScoped
public class CustomerSalesGenerator {

   @Inject
   SalesService salesService;

   @Location("reports/sales/sales_01")
   Template report;

   @Scheduled(every="{kineteco.sales}")
   void generate() {
   }

}
