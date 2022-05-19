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

   @Inject
   ProductSalesGenerator productSalesGenerator;

   @Test
   public void testReportingMail() throws Exception {
      productSalesGenerator.generate();
   }
}