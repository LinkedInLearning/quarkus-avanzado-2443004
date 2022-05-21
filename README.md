# Crear una API GraphQL de búsqueda de datos Imperativa y Reactiva con Quarkus

En este video vamos a aprender a crear busquedas con parametros, a evitar multiples llamadas para buscar un conjunto de datos ademas de cómo extender un API existente sin afectar el cliente.
\
* Get customer
```java
   @Query("getCustomer")
   @Description("Get a Customer")
   public Customer getCustomer(@Name("customerId") String customerId) {
      return Customer.findByCustomerId(customerId);
   }
```
* Query in Dev UI a customer

```json
query{
  getCustomer(customerId: "c1") {
  	 name, customerId
  }
}
```

* Query in Dev UI 2 customers
```json

query getCustomers {
  customer0: getCustomer(customerId: "c1") {
  	 customerId
  }
    customer1: getCustomer(customerId: "c2") {
    name, customerId
    }
}

```
5* En reactivo

Lo suyo seria utilizar el acceso a base de datos reactivo, con hibernate
panache reactive. te invito a que vayas a ver la formacion quarkus esencial.
Podemos usar la anotacion @DefaultValue
```java
  @Query("getCustomerReactive")
   @Description("Get a Customer")
   public Uni<Customer> getCustomerReactive(@Name("customerId") String customerId) {
      return Uni.createFrom().item(Customer.findByCustomerId(customerId));
   }
```

* Extendemos el API usando source
```java
public List<ProductSale> productSales(@Source Customer customer) {
       return CustomerSale.find("customer.customerId", customer.customerId)
            .<CustomerSale>list().stream()
            .map(cs -> cs.productSale)
            .flatMap(List::stream)
            .collect(Collectors.toList());
   }
```

@Source te servira también para poder hacer llamadas en batch. 
No dudes en ir a la documentacion de Quarkus y GraphQL para profundizar en la creación de busquedas para exponer APIs de busqueda de datos eficientes.
