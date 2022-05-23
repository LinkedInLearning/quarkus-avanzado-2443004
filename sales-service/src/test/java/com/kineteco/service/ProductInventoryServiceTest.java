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

   @InjectMock
   @RestClient
   ProductInventoryServiceClient productInventoryServiceClient;

   @CacheName("products")
   Cache products;

   @AfterEach
   public void logCacheContent() {
      products.as(CaffeineCache.class)
               .keySet()
               .forEach(LOGGER::debug);
   }

   @Test
   public void testProductInventory() {
      Product product = new Product();
      product.setSku("KE180");

      when(productInventoryServiceClient.inventory("KE180"))
            .thenReturn(product);

      service.findProduct("KE180");
      service.findProduct("KE180");
      service.findProduct("KE180");

      verify(productInventoryServiceClient,
            times(1)).inventory("KE180");
   }

   @Test
   public void testStock() {
      Product product = new Product();
      product.setSku("KE180");

      when(productInventoryServiceClient.getStock("KE180"))
            .thenReturn(12);

      service.getStock("KE180");
      service.getStock("KE180");
      service.getStockRefreshed("KE180");
      service.getStock("KE180");

      verify(productInventoryServiceClient,
            times(2)).getStock("KE180");
   }
}