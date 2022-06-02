# Utilización de Qute para visualizar reportings periódicos con Quarkus

En este video crearemos una plantilla dinámica usando Qute, el motor de plantillas
de Quarkus.

* Añadimos dependencia `quarkus-resteasy-qute` en pom.xml de Sales Service
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-resteasy-qute</artifactId>
</dependency>
```

* Creamos una plantilla en templates/customer.txt
```<!DOCTYPE html>
hola
```

* Creamos un endpoint

```java

    @Inject
    Template customers;

    @GET
    @Path("/display/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance displayCustomer(@PathParam("id") Long id) {
      return customers;
      }
```

* Modificamos template creamos el customer.txt en `templates`
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
```java
   @GET
    @Path("/display/{id}")
    @Produces(MediaType.TEXT_PLAIN)
    public TemplateInstance displayCustomer(@PathParam("id") Long id) {
      Optional<Customer> customer = Customer.<Customer>findByIdOptional(id);
      String name = customer.map(c -> c.name).orElse("Unknown");
      return customers.data("name", name);
      }
```

* La convertimos a HTML si queremos
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
    </tr>
    <tr>
        <td>{name}</td>
    </tr>
</table>
</body>
</html>
```

* Hacemos una template TypeSafe Dossier CustomerResource

```java
   @CheckedTemplate
    public static class Templates {
        public static native TemplateInstance customers(String name);
    }
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

* Vemos que la tenemos que mover a "CustomerResource"/customers.html