package com.kineteco.service;

import com.kineteco.client.Product;
import com.kineteco.client.ProductInventoryServiceClient;
import io.quarkus.infinispan.client.CacheInvalidate;
import io.quarkus.infinispan.client.CacheResult;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@ApplicationScoped
public class ProductInventoryService {

   private static final Logger LOGGER = Logger.getLogger(ProductInventoryService.class);

   @Inject
   @RestClient
   ProductInventoryServiceClient productInventoryServiceClient;

   @CacheResult(cacheName = "products")
   public Product findProduct(String sku) {
     return productInventoryServiceClient.inventory(sku);
   }

   @CacheResult(cacheName = "stock")
   public Integer getStock(String sku) {
      return productInventoryServiceClient.getStock(sku);
   }

   @CacheInvalidate(cacheName = "stocks")
   @CacheResult(cacheName = "stock")
   public Integer getStockRefreshed(String sku) {
      return productInventoryServiceClient.getStock(sku);
   }
}
