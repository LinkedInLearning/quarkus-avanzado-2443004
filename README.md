# Gestión de errores reactivos con Mutiny y Quarkus
* error
```java
 manufactureOrder.id = sku;

//      if(sku.equals("KI9K")) {
      manufactureOrder.sku = null;
      }
      manufactureOrder.count = quantity;
```
* Transforming exception
```java
 .onFailure().transform(OrderStatsException::new)
```

* Recover With
```java
.onFailure().recoverWithCompletion()

.onFailure().recoverWithItem(() -> onNewStat(null))

```
* Retry
```java
.onFailure().retry().indefinitely()
.onFailure().retry().atMost(3)
```
