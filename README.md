# Añadir trazas distribuídas con OpenTelemetry en Quarkus

* Dependencias
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-opentelemetry-exporter-otlp</artifactId>
</dependency>

<dependency>
    <groupId>io.opentelemetry</groupId>
    <artifactId>opentelemetry-extension-trace-propagators</artifactId>
</dependency>
```

* Configuracion
```properties
quarkus.opentelemetry.enabled=true 
quarkus.opentelemetry.tracer.exporter.otlp.endpoint=http://localhost:4317
```
* Lanzar docker compose de open telemetry con Jaegger

* Ir a la url de Jaegger http://localhost:16686/search

* Usar un SPAN

```java
   @Timed(value = "create-customer-command")
   @WithSpan
   public CustomerSale createCustomerSale(@SpanAttribute(value = "customerCommand") CustomerCommand command, Product product) {
```

Te recuerdo que Opentracing está ahora obsoleto y que debemos de migrar
a OpenTelemetry. 
Te invito a estar al tanto de las últimas novedades de la extension Opentelemetry
de Quarkus en los foros y la web de Quarkus.