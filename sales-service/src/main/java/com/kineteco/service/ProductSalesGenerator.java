package com.kineteco.service;

import com.kineteco.model.ProductSale;
import io.quarkus.logging.Log;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.scheduler.Scheduled;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ProductSalesGenerator {

   @Location("sales/sales-mail-report")
   Template salesReport;

   @Scheduled(cron="{kineteco.sales}")
   public void generate() {
      List<ProductSale> productSales = ProductSale.listAll();
      salesReport.data("sales", productSales)
            .data("date", LocalDateTime.now().toString())
            .renderAsync()
            .thenAccept(r -> Log.info(r))
            .exceptionally(e -> {
               Log.error(e);
               return null;
            });
   }

}
