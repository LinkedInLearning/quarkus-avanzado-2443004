package com.kineteco;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;

import io.restassured.http.ContentType;

import javax.json.Json;
import javax.json.JsonObjectBuilder;

import static io.restassured.RestAssured.given;
import static org.hamcrest.core.Is.is;

@QuarkusTest
public class SalesServiceGraphQLResourceTest {

   @Test
   public void allCustomerSales() {
      String requestBody =
            "{\"query\":" +
                  "\"" +
                  "{" +
                  " allCustomerSales  {" +
                  " id" +
                  "}" +
                  "}" +
                  "\"" +
                  "}";

      given()
            .body(requestBody)
            .post("/graphql/")
            .then()
            .contentType(ContentType.JSON)
            .body("data.allCustomerSales.size()", is(6))
            .statusCode(200);
   }
}