package com.kineteco.service;

import com.kineteco.client.Product;
import com.kineteco.client.ProductInventoryServiceClient;
import io.quarkus.cache.Cache;
import io.quarkus.cache.CacheName;
import io.quarkus.cache.CaffeineCache;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@QuarkusTest
public class ProductInventoryServiceTest {
   private static final Logger LOGGER = Logger.getLogger(ProductInventoryServiceTest.class);

   @Inject
   ProductInventoryService service;

   @Test
   public void testProductInventory() {

   }

   @Test
   public void testStock() {

   }
}