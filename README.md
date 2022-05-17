# Aplicaciones de línea de comandos con Quarkus

* Creamos una clase que implemente QuarkusApplication
* La anotamos con `@QuarkusMain`
* Arrancamos en modo desarrollo
```java
@QuarkusMain
public class SalesCli implements QuarkusApplication {

   @Override
   public int run(String... args) throws Exception {
      return 0;
   }
}
```
```bash
2022-05-10 17:56:01,574 INFO  [io.quarkus] (Quarkus Main Thread) sales-cli 1.0.0-SNAPSHOT on JVM (powered by Quarkus 2.8.3.Final) started in 0.147s. 
2022-05-10 17:56:01,579 INFO  [io.quarkus] (Quarkus Main Thread) Profile dev activated. Live Coding activated.
2022-05-10 17:56:01,579 INFO  [io.quarkus] (Quarkus Main Thread) Installed features: [cdi]
2022-05-10 17:56:01,584 INFO  [io.quarkus] (Quarkus Main Thread) sales-cli stopped in 0.000s
```

* Podemos en vez de implementar QuarkusApplication, implementar un main
Borramos QuarkusMain de Sales Cli

Util para poder lanzar una aplicacion desde el editor. Podemos utilizar esto también para lanzar una aplicacion
en modo debug en el editor y depurar código.

```java
@QuarkusMain
public class SalesCliMain {
   public static void main(String[] args) {
      Quarkus.run(SalesCli.class, args);
   }
}
```

* Para que la app no termine

```java
 Quarkus.waitForExit();
```

* Añadimos test unitario

```java
@QuarkusMainTest
public class SalesCliTest {

   @Test
   @Launch("World")
   public void testLaunchCommand(LaunchResult result) {
      Assertions.assertTrue(result.getOutput().contains("sales-cli 1.0.0-SNAPSHOT on JVM"));
   }
}
```

* Añadimos extension Picocli
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-picocli</artifactId>
</dependency>
```

* Creamos un comando

```java
@ApplicationScoped
@CommandLine.Command(name = "view", description = "View customer data", mixinStandardHelpOptions = true)
public class ViewCustomerCommand implements Runnable {
   Long customerId;

   CustomerService customerService;

   public ViewCustomerCommand(CustomerService customerService) {
      this.customerService = customerService;
   }

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id", defaultValue = "1")
   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
   }

   @Override
   public void run() {
      customerService.displayCustomer(customerId);
   }
}
```
* Lanzamos en linea de comandos

* Creamos un comando para modificar

```java
@ApplicationScoped
@CommandLine.Command(name = "update", description = "Update customer data")
public class ModifyCustomerEmailCommand implements Runnable {

   Long customerId;
   String email;

   @Inject
   CustomerService customerService;

   @CommandLine.Option(names = {"-i", "--id"}, description = "Customer id")
   public void setCustomerId(Long customerId) {
      this.customerId = customerId;
   }

   @CommandLine.Option(names = {"-e", "--email"}, description = "Customer email")
   public void setEmail(String email) {
      this.email = email;
   }

   @Override
   public void run() {
      customerService.updateEmail(customerId, email);
   }
}

```

* Modificamos test unitario
```java
package com.kineteco;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusMainTest
public class SalesCliTest {
   @Test
   @Launch("view")
   public void testLaunchDefaultView(LaunchResult result) {
      Assertions.assertTrue(result.getOutput().contains("email='penatibus.et@lectusa.com"));
   }

   @Test
   @Launch(value = { "view", "--id=2" } )
   public void testLaunchIdArgument(LaunchResult result) {
      Assertions.assertTrue(result.getOutput().contains("email='nibh@ultricesposuere.edu'"));
   }
}
```
* Modificamos SalesCli

```java
@TopCommand
@CommandLine.Command(name = "kineteco", mixinStandardHelpOptions = true, subcommands = {
      ViewCustomerCommand.class,
      ModifyCustomerEmailCommand.class
})
public class SalesCli implements Runnable, QuarkusApplication {

   @Inject
   CommandLine.IFactory factory;

   @Override
   public int run(String... args) throws Exception {
      return new CommandLine(this, factory).execute(args);
   }

   @Override
   public void run() {
      //
   }
}
```

* Build y run en linea de comandos
```java
 ./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar view
java -jar target/quarkus-app/quarkus-run.jar update --id=1 --email=test@test.com
```

