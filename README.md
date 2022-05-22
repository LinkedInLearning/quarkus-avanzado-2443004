# Crear una API GraphQL de modificación de datos con Quarkus

En este video aprenderemos a crear un API de modificacion de datos, además de 
la utilizacion de Subscription para leer los datos desde un websocket.

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
  public Customer createCustomer(Customer customer) {
      customer.persist();
      return customer;
   }
   
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

* En el dev UI
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
    name: "Abel Rodriguez",
    email: "arodriguez.82@gmail.com"
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

subscription newCustomers {
  customerCreation {
    name
  }
  
}
```

Recuerda que l integracion con GrapqhQL está en activo desarrollo y que las las funcionalidaes
y utilidades van mejorando. Te invito a estar al tanto de las últimas modifiaciones de GraphQL
en los foros y las versiones de Quarkus.
