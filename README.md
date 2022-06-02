# Utilización de Qute para visualizar reportings periódicos con Quarkus
- En este video aprenderemos a utilizar Qute para crear y poder visualizar
reporting periódicos de datos con Quarkus.
Te invito a que repases los vídeos de este mismo curso dedicados a las tareas 
periodicas y a la introducción de Qute.

- En estos momentos estamos sacando el reporte periodico con el Log, pero podremos
imaginar que guardamos el reporte en un sistema de ficheros o que lo enviamos
por email.
El lenguaje del motor de plantillas es bastante completo. No dudes en consultar
la documentación de Quarkus para implementar tus propios casos de uso con
Qute y las tareas periódicas.

* Añadimos dependencia `quarkus-resteasy-qute` en pom.xml de Sales Service
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-qute</artifactId>
</dependency>
```

* Tenemos un report de sales sales-mail-report

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>Sales Reporting</title>
</head>
<body>
<h1>Sales Reporting {date}</h1>
<table>
    <tr>
        <td>Sku</td>
        <td>Name</td>
        <td>Price</td>
    </tr>
{#for sale in sales}
        <tr>
            <td>{sale.sku ?: 'Unknown'}</td>
            <td>{sale.name ?: 'Unknown'}</td>
            <td>
                {#if (sale.total > 500)}
                    <label>Popular</label>
                {/if}
                {sale.total}
            </td>
        </tr>
{/for}
</table>
</body>
</html>
```

* SalesGenerator
```java
package com.kineteco.service;

import com.kineteco.CustomerResource;
import com.kineteco.model.ProductSale;
import io.quarkus.qute.Location;
import io.quarkus.qute.Template;
import io.quarkus.scheduler.Scheduled;
import org.jboss.logging.Logger;

import javax.enterprise.context.ApplicationScoped;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@ApplicationScoped
public class ProductSalesGenerator {

   private static final Logger LOGGER = Logger.getLogger(CustomerResource.class);

   @Location("sales/sales-mail-report")
   Template salesReport;

   @Scheduled(cron="{kineteco.sales}")
   public void generate() {
      List<ProductSale> productSales = ProductSale.listAll();
      salesReport
            .data("sales", productSales)
            .data("date", LocalDateTime.now().toString())
            .renderAsync()
            .thenAccept(r -> LOGGER.info(r))
            .exceptionally(e -> {
               LOGGER.error(e);
               return null;
            });
   }

}

```
