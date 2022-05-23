package com.kineteco.service;

import com.kineteco.client.ProductInventoryServiceClient;
import io.quarkus.test.junit.QuarkusTest;
import io.quarkus.test.junit.mockito.InjectMock;
import org.eclipse.microprofile.rest.client.inject.RestClient;
import org.junit.jupiter.api.Test;

import javax.inject.Inject;

@QuarkusTest
public class ProductInventoryServiceTest {

   @Inject
   ProductInventoryService productInventoryService;

   @InjectMock
   @RestClient
   ProductInventoryServiceClient productInventoryServiceClient;

   @Test
   public void pepepe() {
      productInventoryService.displayCacheContent();
   }
}