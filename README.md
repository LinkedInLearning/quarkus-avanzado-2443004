# Utilización de anotaciones de caché con Quarkus

* Dependencia en Sales Service
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-cache</artifactId>
</dependency> 
```

* Test
```java
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
```

* Product Inventory Service
```java
@ApplicationScoped
public class ProductInventoryService {
   public static final String PRODUCTS = "products";
   public static final String STOCK = "stock";

   @Inject
   @RestClient
   ProductInventoryServiceClient productInventoryServiceClient;

   @CacheResult(cacheName = PRODUCTS)
   public Product findProduct(String sku) {
      return productInventoryServiceClient.inventory(sku);
   }

   @CacheResult(cacheName = STOCK)
   public Integer getStock(String sku) {
      return productInventoryServiceClient.getStock(sku);
   }

   @CacheInvalidate(cacheName = STOCK)
   @CacheResult(cacheName = STOCK)
   public Integer getStockRefreshed(String sku) {
      return productInventoryServiceClient.getStock(sku);
   }

}
```

