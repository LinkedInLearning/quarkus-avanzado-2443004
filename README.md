# Medir la cobertura de tests en Quarkus

* Añadimos dependencia `quarkus-jococo` en pom.xml 
```xml
<dependency>
    <groupId>io.quarkus</groupId>
    <artifactId>quarkus-jacoco</artifactId>
    <scope>test</scope>
</dependency>
```

* mvn clean verify y mirar en target

* Configuramos 
```properties
quarkus.jacoco.title=Sales Service
quarkus.jacoco.footer=Kineteco
```

No hemos tenido que añadir nada especial en el pom.xml

Para terminar te diré que para cambiar los parametros por defecto de los ratios,
incluir coverage de los test de integracion nativos o incluir tests que no
lleven las anotaciones de test de Quarkus, tendràs que añadir configuracion adicional
en el pom.xml y encontrarás toda la documentacion en la documentación de Quarkus.
Recuerda que una buena covertura de test no significa que la aplicación esté bien
testeada, sin embargo es una buena métrica a tener en cuenta desde el inicio.

 