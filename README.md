# Utilización de Qute para visualizar reportings periódicos con Quarkus

* Añadimos dependencia `quarkus-resteasy-qute` en pom.xml de Sales Service
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-qute</artifactId>
</dependency>
```

* Creamos un endpoint rest para visualizar un cliente
```java

   @Inject
    Template customers;

    @GET
    @Path("/display/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance get(@PathParam("id") Long id) {
      Optional<Customer> customer = Customer.<Customer>findByIdOptional(id);
      String name = customer.map(c -> c.name).orElse("Unknown");
      return customers.data("name", name);
      }
```

* En resources template creamos el customer.txt en `templates`
```text
{name}
```
* Lo convertimos a html
```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>{name}</title>
</head>
<body>
<h1>{name}</h1>
</body>
</html>
```

* Injectamos la template
```html
@Inject
Template customers;
```

* Hacemos una template TypeSafe Dossier CustomerResource

```java
@CheckedTemplate
    public static class Templates {
        public static native TemplateInstance customers(String name, String email);
    }
```

```html
<!DOCTYPE html>
<html>
<head>
    <meta charset="UTF-8">
    <title>{name}</title>
</head>
<body>
<h1>Customer Information</h1>
<table>
    <tr>
        <td><h3>Name</h3></td>
        <td><h3>Email</h3></td>
    </tr>
    <tr>
        <td>{name}</td>
        <td>{email}</td>
    </tr>
</table>
</body>
</html>
```

```java
@GET
@Path("/display/{id}")
@Produces(MediaType.TEXT_PLAIN)
public TemplateInstance get(@PathParam("id") Long id) {
      Optional<Customer> customer = Customer.<Customer>findByIdOptional(id);
      String name = customer.map(c -> c.name).orElse("Unknown");
      String email = customer.map(c -> c.email).orElse("Unknown");
      //        return customers.data("name", name);
      return Templates.customers(name, email);
      }
```

* Creamos un report de sales sales-mail-report

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