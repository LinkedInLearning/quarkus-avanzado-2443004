# Consumir una API GraphQL con el cliente GraphQL de Quarkus
En este video vamos a aprender a consumir un API GraphQL utlizando el cliente java
además de cómo gestionar los posibles errores del API.

* Extension
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-smallrye-graphql-client</artifactId>
</dependency> 
```
* Configurar
```properties
quarkus.smallrye-graphql-client.customers.url=http://localhost:8280/graphql
```

* Crear interfaz
```java
@GraphQLClientApi(configKey = "customers")
public interface CustomerClient {

   @Query(value="getCustomer")
   Customer customerDetail(String customerId);
}
```

* Utilizar cliente 
```java
@ApplicationScoped
public class CustomerService {

   @Inject
   CustomerClient customerClient;

   public void displayCustomer(String customerId) {
      System.out.println(customerClient.customerDetail(customerId));
   }
```

* Command line
```
view --id=c1 
view --id=c32 
```

* Modificacion
```java
public interface CustomerClient {
   @Mutation(value="createOrUpdateCustomer")
   Customer updateEmail(Customer customer);
}
```

````java
public void updateEmail(String id, String email) {
      Customer customer = customerClient.customerDetail(id);
      customer.email = email;
      Customer updated = customerClient.updateEmail(customer);
      System.out.println("Email updated " + updated);
   }
````

update --id=c1 --email=nuevo@test.com

* Cliente dinamico
```java
   @Inject
   @GraphQLClient("customers")
   DynamicGraphQLClient dynamicCustomerClient;

         Document query = document(
         operation(
             field("getCustomer",
                 List.of(arg("customerId", customerId)),
                 field("id"),
                 field("name"),
                 field("email"),
                 field("customerId"))
             )
         );
         
         try {
             Response response = dynamicCustomerClient.executeSync(query);
             System.out.println(response);
             System.out.println(response.getObject(Customer.class, "getCustomer"));
          } catch (Exception e) {
            throw new RuntimeException(e);
          }
```

* Un nuevo error
```properties
quarkus.smallrye-graphql.default-error-message=Kineteco exception
```
```java
@ErrorCode("customer-not-found")
public class CustomerNotFoundException extends RuntimeException {
}

```
* Modify
```java
  @Query("getCustomer")
   @Description("Get a Customer")
   public Customer getCustomer(@Name("customerId") String customerId) {
      return Customer.findByCustomerId(customerId)
            .orElseThrow(() -> new CustomerNotFoundException());
   }
```
* Test
```
view --id=c42
update --id=c42 --email=nuevo@test.com
```

* Parse error 
```java
 ErrorOr<Customer> customerDetail(String customerId);

public void updateEmail(String id, String email) {
      ErrorOr<Customer> customerErrorOr = customerClient.customerDetail(id);
      if (customerErrorOr.hasErrors()) {
          System.out.println(customerErrorOr.getErrors());
          return;
      }
      Customer customer = customerErrorOr.get();
      customer.email = email;
      Customer updated = customerClient.updateEmail(customer);
      System.out.println("Email updated " + updated);
}
```

En este video hemos aprendido cómo la flexbilidad y la potencia del cliente de GraphQL de 
Quarkus nos permite consumir APIs GraqphQL con Java de forma eficaz. 
