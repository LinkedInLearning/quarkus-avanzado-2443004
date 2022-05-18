# Aplicaciones de línea de comandos con Quarkus y Picocli

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
package com.kineteco;

import io.quarkus.picocli.runtime.annotations.TopCommand;
import picocli.CommandLine;

@TopCommand
@CommandLine.Command(name = "kineteco",
      mixinStandardHelpOptions = true,
      subcommands = {
            ViewCustomerCommand.class,
            ModifyCustomerEmailCommand.class
      })
public class SalesCliEntryCommand {
}
```

* Build y run en linea de comandos
```java
 ./mvnw clean package
java -jar target/quarkus-app/quarkus-run.jar view
java -jar target/quarkus-app/quarkus-run.jar update --id=1 --email=test@test.com
```

