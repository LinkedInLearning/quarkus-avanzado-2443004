package com.kineteco.service;

import io.quarkus.mailer.Mail;
import io.quarkus.mailer.MockMailbox;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@QuarkusTest
public class ProductSalesGeneratorTest {

   private static final String TO = "sales@kineteco.com";

   @Inject
   MockMailbox mailbox;

   @Inject
   ProductSalesGenerator productSalesGenerator;

   @BeforeEach
   void init() {
      mailbox.clear();
   }

   @Test
   public void testReportingMail() throws Exception {
      productSalesGenerator.generate();

      List<Mail> sent = mailbox.getMessagesSentTo(TO);
      assertThat(sent).hasSize(1);
      Mail actual = sent.get(0);
      assertThat(actual.getSubject()).isEqualTo("Daily Report");
      assertThat(actual.getHtml()).contains("Sales Reporting");

      assertThat(mailbox.getTotalMessagesSent()).isEqualTo(1);
   }
}