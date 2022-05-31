# Backpressure con Mutiny y Quarkus

```java
 for (int i = 0; i < 200; i++){
         emitter.send(manufactureOrder);
      }
```
* Drop 
```java
.onOverflow().invoke(() -> Log.error("No me da la vida")).drop();
```
* Buffer
```java
.onOverflow().invoke(() -> Log.error("No me da la vida")).buffer(10)
```