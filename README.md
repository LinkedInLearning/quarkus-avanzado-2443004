# Crear una API GraphQL de modificación de datos con Quarkus

En este video vamos a aprender a crear busquedas con parametros, a evitar multiples llamadas para buscar un conjunto de datos ademas de cómo extender un API existente sin afectar el cliente.


```json
mutation create {
      createCustomer (customer: {
        customerId: "c4",
        name: "Abel Rodriguez",
        email: "arodriguez.82@gmail.com"
      }) {
      id
    }
}
mutation update {
    createCustomer (customer: {
        id:1
        customerId: "c4",
        name: "William Butfrozen",
        email: "william.butfrozen@kineteco.com"
        }) {
    id
    }
}

query getCustomer {
    getCustomer(customerId:"c4") {
            id,
            name,
            email,
            customerId
        }
}

mutation deleteCustomer {
  deleteCustomer(id:1)
}
```

* Codigo Java 2 métodos

```java
 @Mutation
   @Transactional
   public Customer createCustomer(Customer customer) {
      if (customer.id == null) {
         customer.persist();
      } else {
         Customer existing = Customer.findById(customer.id);
         existing.name = customer.name;
         existing.email = customer.email;
      }
      return customer;
   }

   @Mutation
   @Transactional
   public boolean deleteCustomer(@Name("id") Long id) {
      return Customer.deleteById(id);
   }
```

* Experimental: Subscription

```java
 BroadcastProcessor<Customer> processor = BroadcastProcessor.create();

 @Subscription
   public Multi<Customer> customerCreation(){
      return processor;
      }
      
```