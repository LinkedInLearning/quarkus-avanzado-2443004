package com.kineteco.service;

import com.kineteco.rest.CustomerResource;
import com.kineteco.model.ProductSale;
import io.quarkus.mailer.MailTemplate;
import io.quarkus.mailer.Mailer;
import io.quarkus.qute.Location;
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
   MailTemplate salesReport;
//   Template salesReport;

   @Inject
   Mailer mailer;

   @Scheduled(cron="{kineteco.sales}")
   public void generate() {
      List<ProductSale> productSales = ProductSale.listAll();
      Uni<Void> send = salesReport
            .to("sales@kineteco.com")
            .subject("Daily Report").data("sales", productSales)
            .data("date", LocalDateTime.now().toString())
            .send();

      send.await().atMost(Duration.ofMinutes(1));
//      send.await().indefinitely();
      // Generate Report
//      salesReport
//            .data("sales", productSales)
//            .data("date", LocalDateTime.now().toString())
//            .renderAsync()
//            .thenAccept(r ->
//               mailer.send(Mail
//                     .withHtml("sales@kineteco.com",
//                           "Daily Reporting", r))
//            )
//            .exceptionally(e -> {
//               LOGGER.error(e);
//               return null;
//            });

   }

}
