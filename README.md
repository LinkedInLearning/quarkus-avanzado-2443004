# Utilización de Qute para visualizar reportings periódicos con Quarkus

* Añadimos dependencia `quarkus-resteasy-qute` en pom.xml de Sales Service
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-mailer</artifactId>
</dependency>
```

* Modificamos el envio utilizando la injeccion

```java

@Inject
Mailer mailer;
ReactiveMailer;

 @Location("sales/sales-mail-report")
MailTemplate salesReport;

Uni<Void> send = salesReport
      .to("sales@kineteco.com")
      .subject("Daily Report").data("sales", productSales)
      .data("date", LocalDateTime.now().toString())
      .send();

      send.await().atMost(Duration.ofMinutes(1));
```

* Test
```java
@QuarkusTest
public class ProductSalesGeneratorTest {

   private static final String TO = "sales@kineteco.com";

   @Inject
   MockMailbox mailbox;

   @Inject
   ProductSalesGenerator productSalesGenerator;

   @BeforeEach
   void init() {
      mailbox.clear();
   }

   @Test
   public void testReportingMail() throws Exception {
      productSalesGenerator.generate();

      List<Mail> sent = mailbox.getMessagesSentTo(TO);
      assertThat(sent).hasSize(1);
      Mail actual = sent.get(0);
      assertThat(actual.getSubject()).isEqualTo("Daily Report");
      assertThat(actual.getHtml()).contains("Sales Reporting");

      assertThat(mailbox.getTotalMessagesSent()).isEqualTo(1);
   }
}
```


Te invito a que vayas a la documentación de Quarkus para configurar GMAIL, 
las opciones SMTP y credenciales que necesite tu aplication Quarkus. 