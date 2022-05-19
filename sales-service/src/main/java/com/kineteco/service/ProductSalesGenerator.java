package com.kineteco.service;

import com.kineteco.CustomerResource;
import com.kineteco.model.ProductSale;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProductSalesGenerator {

   private static final Logger LOGGER = Logger.getLogger(CustomerResource.class);



   @Scheduled(cron="{kineteco.sales}")
   public void generate() {
      List<ProductSale> productSales = ProductSale.listAll();

      // Generate Report
   }

}
