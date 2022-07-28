# Utilización de anotaciones de caché con Quarkus

La utilización de las anotacions con Infinispan es una funcionalidad en desarrollo que será incluida en la version 2.12 de
Quarkus junto con Infinispan 13 o Infinispan 14 si la release de 14 es Final.
El repositorio se actualizará con la version.

La pull request: https://github.com/quarkusio/quarkus/pull/25300

```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-infinispan-client</artifactId>
</dependency> 
```
* Run dev mode. Create 2 caches consola
* Inventory Service cambiamos los paquetes
```java
import io.quarkus.infinispan.client.CacheInvalidate;
import io.quarkus.infinispan.client.CacheResult;
```

* Schema
```java
package com.kineteco.client;

import org.infinispan.protostream.GeneratedSchema;
import org.infinispan.protostream.annotations.AutoProtoSchemaBuilder;
import org.infinispan.protostream.types.java.math.BigDecimalAdapter;

@AutoProtoSchemaBuilder(includeClasses= { Product.class, BigDecimalAdapter.class },
      schemaPackageName = "kineteco")
public interface ProductSchema extends GeneratedSchema {
}
```

* Annotations

```java
package com.kineteco.client;

import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
   private String sku;
   private String name;
   private String productLine;
   private BigDecimal price;

   public Product() {
   }

   @ProtoFactory
   public Product(String sku, String name, String productLine, BigDecimal price) {
      this.sku = sku;
      this.name = name;
      this.productLine = productLine;
      this.price = price;
   }

   @ProtoField(1)
   public String getSku() {
      return sku;
   }

   @ProtoField(2)
   public String getProductLine() {
      return productLine;
   }

   @ProtoField(3)
   public BigDecimal getPrice() {
      return price;
   }

   @ProtoField(4)
   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public void setProductLine(String productLine) {
      this.productLine = productLine;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Product product = (Product) o;
      return Objects.equals(sku, product.sku) && Objects.equals(name, product.name) && Objects.equals(productLine,
            product.productLine) && Objects.equals(price, product.price);
   }

   @Override
   public int hashCode() {
      return Objects.hash(sku, name, productLine, price);
   }

   @Override
   public String toString() {
      return "Product{" + "sku='" + sku + '\'' + ", name='" + name + '\'' + ", productLine='" + productLine + '\''
            + ", price=" + price + '}';
   }
}
```

* Test

```shell
http 'localhost:8280/sales/KE180/availability?units=100' 

http post localhost:8280/sales customerId=c1 sku=KE180 units=12 
http post localhost:8280/sales customerId=c1 sku=KE275 units=12 
http post localhost:8280/sales customerId=c1 sku=KE300 units=12
http post localhost:8280/sales customerId=c1 sku=KE700 units=12
 
```

* Verify Console