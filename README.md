# Implementar una primera API con GraphQL y Quarkus

* Añadimos dependencia `quarkus-smallrye-graphql` en pom.xml 
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-graphql</artifactId>
</dependency>
```

* Creamos un primer endpoint graphql

```java
@GraphQLApi
public class SalesServiceGraphQLResource {

   @Query("allCustomerSales")
   @Description("Get all product sales")
   public List<CustomerSale> getAllCustomerSales() {
      return CustomerSale.listAll();
   }
}
```
 
* En el Dev UI

Hacemos una query con el id
Intentamos hacer uno mal
Cambiamos el tipo de dato de salida
