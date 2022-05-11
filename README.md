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
