# Añadir métricas por defecto con Micrometer en Quarkus

* Dependencias en SalesService y ProductInventory

```xml
<dependency>
  <groupId>io.quarkus</groupId>
  <artifactId>quarkus-micrometer-registry-prometheus</artifactId>
</dependency>
```


* Vemos el endpoint 
```shell
http localhost:8280/q/metrics   
```

* Como usamos la extension caché, vamos a configurar las métricas
```properties
quarkus.smallrye-graphql.metrics.enabled=true

quarkus.cache.caffeine."products".metrics-enabled=true
quarkus.cache.caffeine."stock".metrics-enabled=true
```
* Probamos grapqhQL con el DEV UI
```shell
http localhost:8280/q/metrics | grep graphql 
```
* 
* Probamos en linea de commandos
```shell
http post localhost:8280/sales customerId=c1 sku=KE180 units=12   
http post localhost:8280/sales customerId=c1 sku=KE325X units=12
http post localhost:8280/sales customerId=c1 sku=KE325X units=12
http  localhost:8280'/sales/KE180/availability?units=12'

http localhost:8280/q/metrics | grep products 
http localhost:8280/q/metrics | grep stock 
```