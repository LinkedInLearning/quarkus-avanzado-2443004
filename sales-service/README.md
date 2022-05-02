# Quarkus esencial
## 07_06 Tolerancia a fallos con Quarkus: Bulkhead

* Ponemos la anotación @Bulkhead(1) para permitir una sola llamada cada vez
* Arrancamos Product Inventory
* Arrancamos Sales Service (atencion -Ddebug=5001)

Llamamos al endpoint sales con un producto que es DELUXE
```shell
http post localhost:8081/sales customerId=123 sku=KEBL400x units=30
```

Llamamos al endpoint sales con un producto que es ECONOMY

```shell
http post localhost:8081/sales customerId=123 sku=KEBL600 units=30 
```
Vamos a llamarlos varias veces en paralelo con scripts. Vemos como tenemos
Cambiamos la exception

```java
     if (context.getFailure() instanceof BulkheadException) {
         return Response.status(Response.Status.TOO_MANY_REQUESTS).build();
      }
```
Probamos  y vemos como bulkhead se activa pero ademas circuit breaker