package com.kineteco.service;

import com.kineteco.CustomerResource;
import com.kineteco.model.ProductSale;
import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MailTemplate;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.CheckedTemplate;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.scheduler.Scheduled;
import io.smallrye.mutiny.Uni;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@ApplicationScoped
public class ProductSalesGenerator {

   private static final Logger LOGGER = Logger.getLogger(CustomerResource.class);

   @Location("sales/sales-mail-report")
   Template salesReport;

   @Scheduled(cron="{kineteco.sales}")
   public void generate() {
      List<ProductSale> productSales = ProductSale.listAll();
      // Generate Report
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
