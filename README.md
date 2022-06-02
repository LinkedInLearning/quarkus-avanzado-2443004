# Crear tus propias métricas con Micrometer en Quarkus

* Count
```java
@ApplicationScoped
public class MetricsService {

    @Inject
    MeterRegistry registry;

    public void countStocks(int stock) {
        if (stock == 1 ) {
            registry
            .counter("kineteco.stock.number", "type", "one")
            .increment();
        }
    
          if (stock > 100 ) {
             registry
                   .counter("kineteco.stock.number", "type", "greater100")
                   .increment();
          }
    }
}
```
* Modificamos Sales Service REST

```java
   @GET
    @Produces(MediaType.TEXT_PLAIN)
    @Path("/{sku}/availability")
    @Timeout(value = 100)
    @Retry(retryOn = TimeoutException.class, delay = 100, jitter = 25)
    @Fallback(value = SalesServiceFallbackHandler.class)
    public Response available(@PathParam("sku") String sku, @QueryParam("units") Integer units) {
        LOGGER.debugf("available %s %d", sku, units);
        if (units == null) {
            throw new BadRequestException("units query parameter is mandatory");
        }

        metricsService.countStocks(units);

        return Response.ok(productInventoryService.getStock(sku) >= units).build();
    }
```

* Probamos

```shell
http  localhost:8280'/sales/KE180/availability?units=1
http  localhost:8280'/sales/KE180/availability?units=1
http  localhost:8280'/sales/KE180/availability?units=200
http  localhost:8280'/sales/KE180/availability?units=1
http  localhost:8280'/sales/KE180/availability?units=40
http  localhost:8280'/sales/KE180/availability?units=4

http localhost:8280/q/metrics | grep kineteco.stock.number  
```

* Usar anotaciones

```java
@ApplicationScoped
public class SalesService {

   @Timed(value = "create-customer-command")
```

* Probamos
```java
http post localhost:8280/sales customerId=c1 sku=KE325X units=12
http post localhost:8280/sales customerId=c1 sku=KE325X units=12
http post localhost:8280/sales customerId=c1 sku=KE325X units=12
http post localhost:8280/sales customerId=c7 sku=KE325X units=12
```