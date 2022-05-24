# Mejora el rendimiento de tu base de datos existente con Quarkus e Infinispan

* Instalamos el operador de infinispan

https://operatorhub.io/operator/infinispan

* Creamos un infinispan
```shell
kubectl create secret generic --from-file=identities.yaml connect-secret
kubectl apply -f infinispan.yaml 
```

* Vamos a la consola web

* Configuramos dos caches
```shell
kubectl apply -f stock.yaml 
kubectl apply -f products-cache.yaml 
```

* Sales Service conectamos Infinispan
```properties
%prod.quarkus.infinispan-client.server-list=infinispan-external:11222
%prod.quarkus.infinispan-client.auth-username=admin
%prod.quarkus.infinispan-client.auth-password=secret
#%prod.quarkus.infinispan-client.auth-username=${infinispan-username}
#%prod.quarkus.infinispan-client.auth-password=${infinispan-password}
```

* Probamos
```shell
export SALES_SERVICE=$(minikube service --url sales-service)  
http $SALES_SERVICE'/sales/KE180/availability?units=12' 
http post $SALES_SERVICE/sales customerId=c1 sku=KE180 units=12
http post $SALES_SERVICE/sales customerId=c1 sku=KE325X units=12
```

* Vamos a crear una cache table store

```shell
http $SALES_SERVICE/customers
 
kubectl apply -f customers-cache.yaml 
```

* Vamos a crear un nuevo tipo y endpoint
```java
@AutoProtoSchemaBuilder(includeClasses= { Customer.class },
      schemaPackageName = "db.kineteco")
public interface DBSchema extends GeneratedSchema {
}

@RegisterForReflection
@Entity
public class Customer extends PanacheEntity {
   @ProtoField(1)
   public String name;
   @ProtoField(2)
   public String customerId;
   @ProtoField(3)
   public String email;

   public Customer() {
   }

   @ProtoFactory
   public Customer(String customerId, String name, String email) {
      this.customerId = customerId;
      this.name = name;
      this.email = email;
   }

   public static Optional<Customer> findByCustomerId(String customerId) {
      return find("customerId", customerId).firstResultOptional();
   }
}

Customer Resource
   @Inject
   @Remote("customers")
   RemoteCache<Long, Customer> customers;

   @GET
   @Produces(MediaType.APPLICATION_JSON)
   @Path("/memory")
   public Response customersFromMemory() {
      return Response.ok(customers.entrySet()).build();
   }
```
* Creamos una DB