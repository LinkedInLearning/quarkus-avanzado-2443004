package com.kineteco;

import com.kineteco.model.ConsumerType;
import com.kineteco.model.ProductInventory;
import com.kineteco.model.ProductLine;
import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import javax.ws.rs.core.Response;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;

@QuarkusTest
public class ProductInventoryResourceTest {

    @Test
    public void testHealthEndpoint() {
        given()
          .when().get("/products/health")
          .then()
             .statusCode(200)
             .body(is("Product Inventory Service is up!"));
    }

    @Test
    public void inventoryEndpoint() {
        ProductInventory domesticProduct = given().when().get("/products/{sku}", "KE180")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().body().as(ProductInventory.class);
        assertThat(domesticProduct.sku).isEqualTo("KE180");
        assertThat(domesticProduct.targetConsumer).containsExactly(ConsumerType.DOMESTIC);

        given().when().get("/products/{sku}", "foo")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testListProducts() {
        ProductInventory[] inventory = given().when()
              .get("/products")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .body("$.size()", is(53))
              .extract().as(ProductInventory[].class);
        assertThat(inventory).hasSize(53);
        assertThat(inventory[0].name).isEqualTo("K-Eco 12-volt Lithium-ion solar backup battery");
    }

    @Test
    public void testCRUD() {
        given()
              .body("{\"sku\": \"123\", \"name\": \"product\"}")
              .contentType(ContentType.JSON)
              .when()
              .post("/products")
              .then()
              .statusCode(Response.Status.CREATED.getStatusCode());

        ProductInventory createdProduct = given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(Response.Status.OK.getStatusCode()).extract()
              .body().as(ProductInventory.class);
        assertThat(createdProduct.sku).isEqualTo("123");
        assertThat(createdProduct.name).isEqualTo("product");

        given()
              .body("{\"name\": \"Super Product\" }")
              .contentType(ContentType.JSON)
              .when()
              .put("/products/{sku}", "123")
              .then()
              .statusCode(Response.Status.OK.getStatusCode());

        ProductInventory updatedProduct = given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(Response.Status.OK.getStatusCode()).extract()
              .body().as(ProductInventory.class);

        assertThat(updatedProduct.sku).isEqualTo("123");
        assertThat(updatedProduct.name).isEqualTo("Super Product");

        given().when().delete("/products/{sku}", "123")
              .then()
              .statusCode(Response.Status.NO_CONTENT.getStatusCode());

        given().when().get("/products/{sku}", "123")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testUpgradeStock() {
        ProductInventory productInventoryPre = given().when().get("/products/{sku}", "KE200")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().body().as(ProductInventory.class);

        assertThat(productInventoryPre).isNotNull();

        given()
              .when().queryParam("stock", 3)
              .patch("/products/{sku}", "KE200")
              .then()
              .statusCode(Response.Status.ACCEPTED.getStatusCode());

        ProductInventory productInventoryAfter = given().when().get("/products/{sku}", "KE200")
              .then()
              .statusCode(Response.Status.OK.getStatusCode())
              .extract().body().as(ProductInventory.class);

        assertThat(productInventoryAfter.unitsAvailable).isEqualTo(productInventoryPre.unitsAvailable + 3);
    }

    @Test
    public void testUpdateNotFound() {
        given().when().get("/products/{sku}", "foo")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testUpgradeStockFound() {
        given().when().queryParam("stock", 3)
              .patch("/products/{sku}", "foo")
              .then()
              .statusCode(Response.Status.NOT_FOUND.getStatusCode());
    }

    @Test
    public void testCountProductLine() {
        given()
              .when().get("/products/line/{productLine}", ProductLine.DELUXE)
              .then()
              .statusCode(200)
              .body(is("3"));
    }

    @Test
    public void getProductStock() {
        given()
              .when().get("/products/{sku}/stock", "KE180")
              .then()
              .statusCode(200)
              .body(is("1456"));
    }

    @Test
    public void testPaginationProducts() {
        ProductInventory[] inventory =
              given().queryParam("page", 1)
                    .queryParam("size", 10)
                    .when().get("/products/")
                    .then().statusCode(200)
                    .extract().as(ProductInventory[].class);

        assertThat(inventory).hasSize(10);
        assertThat(inventory[0].name).isEqualTo("K-Eco 325x (same power smaller footprint)");
    }
}
