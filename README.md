# Configurar y ejecutar tareas periódicas con Quarkus

* Añadimos dependencia `quarkus-scheduler` en pom.xml de Sales Service
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-scheduler</artifactId>
</dependency>
```

* Añadimos un método que se ejecute cada 0.5 segundos
```java
@ApplicationScoped
public class Scheduler {
   @Scheduled(every="0.5s")
   void increment() {
      System.out.println(Customer.count());
   }
}
```
Podemos pasar una expresion 'cron' 

* Configuramos en el application properties
```java
@Scheduled(every="{kineteco.task}")
```
```properties
kineteco.task=10s
```
* Si queremos desactivarlo en produccion
```properties
%prod.kineteco.task=off
```

* Delayed execution
```java
@Scheduled(delay = 2, delayUnit = TimeUnit.HOUR)
@Scheduled(delayed="2h")
```

* Condiciones
```java
@ApplicationScoped
public class Scheduler {
   @Scheduled(every="{kineteco.task}", skipExecutionIf = SkipIfNotDev.class)
   void increment() {
      System.out.println(Customer.count());
   }
}

@Singleton
class SkipIfNotDev

}

@Singleton
class SkipIfNotDev implements Scheduled.SkipPredicate {
   public boolean test(ScheduledExecution execution) {
      return !"dev".equals(ProfileManager.getActiveProfile());
   }
}
```
Para persistencia: Quarz
