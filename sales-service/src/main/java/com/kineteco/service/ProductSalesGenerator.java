package com.kineteco.service;

import com.kineteco.CustomerResource;
import com.kineteco.model.ProductSale;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class ProductSalesGenerator {

   private static final Logger LOGGER = Logger.getLogger(CustomerResource.class);

   @Location("sales/sales-mail-report")
   Template salesReport;

   @Scheduled(cron="{kineteco.sales}")
   public void generate() {
      List<ProductSale> productSales = ProductSale.listAll();
      salesReport
            .data("sales", productSales)
            .data("date", LocalDateTime.now().toString())
            .renderAsync()
            .thenAccept(r -> LOGGER.info(r))
            .exceptionally(e -> {
               LOGGER.error(e);
               return null;
            });
   }

}
