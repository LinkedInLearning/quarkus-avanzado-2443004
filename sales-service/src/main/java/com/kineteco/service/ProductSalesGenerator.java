package com.kineteco.service;

import com.kineteco.model.ProductSale;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import java.util.List;

@ApplicationScoped
public class ProductSalesGenerator {

   @Scheduled(cron="{kineteco.sales}")
   public void generate() {
      List<ProductSale> productSales = ProductSale.listAll();

      // Generate Report
   }

}
