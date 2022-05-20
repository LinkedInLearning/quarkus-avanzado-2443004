package com.kineteco;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.stubbing.Scenario;
import io.quarkus.test.common.QuarkusTestResourceLifecycleManager;

import javax.json.Json;
import javax.json.JsonObjectBuilder;
import javax.ws.rs.core.Response;
import java.util.Collections;
import java.util.Map;

import static com.github.tomakehurst.wiremock.client.WireMock.aResponse;
import static com.github.tomakehurst.wiremock.client.WireMock.get;
import static com.github.tomakehurst.wiremock.client.WireMock.serviceUnavailable;
import static com.github.tomakehurst.wiremock.client.WireMock.stubFor;
import static com.github.tomakehurst.wiremock.client.WireMock.urlEqualTo;

public class ProductInventoryWiremock implements QuarkusTestResourceLifecycleManager {
   private WireMockServer wireMockServer;

   @Override
   public Map<String, String> start() {
      wireMockServer = new WireMockServer();
      wireMockServer.start();
      stubOk();
      stubTimeout();
      stubRetry();
      stubFallback();
      stubCircuitBreaker();
      stubSecured();
      return Collections.singletonMap("kineteco-product-inventory/mp-rest/url", wireMockServer.baseUrl());
   }

   private void stubOk() {
      stubFor(get(urlEqualTo("/products/123/stock"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("42")
            ));

      stubFor(get(urlEqualTo("/products/456/stock"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody("2")
            ));
   }

   static void stubTimeout() {
      stubFor(get(urlEqualTo("/products/falloTimeout/stock"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(150)
                  .withBody("42")
            ));
   }

   static void stubRetry() {
      stubFor(get(urlEqualTo("/products/falloRetry/stock"))
            .inScenario("retry")
            .whenScenarioStateIs(Scenario.STARTED)
            .willSetStateTo("120-milliseconds")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(150)
                  .withBody("42")
            ));

      stubFor(get(urlEqualTo("/products/falloRetry/stock"))
            .inScenario("retry")
            .whenScenarioStateIs("120-milliseconds")
            .willSetStateTo("50-milliseconds")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(120)
                  .withBody("42")
            ));

      stubFor(get(urlEqualTo("/products/falloRetry/stock"))
            .inScenario("retry")
            .whenScenarioStateIs("50-milliseconds")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(50)
                  .withBody("42")
            ));
   }

   static void stubFallback() {
      stubFor(get(urlEqualTo("/products/fallback_1/stock"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(150)
                  .withBody("42")
            ));

      stubFor(get(urlEqualTo("/products/fallback_2/stock"))
            .willReturn(serviceUnavailable()));
   }

   static void stubCircuitBreaker() {

      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
      objectBuilder
            .add("sku", "circuitBreaker")
            .add("productLine", "DELUXE")
            .add("name", "circuitBreaker")
            .add("price", 12.8);
      String productJson = objectBuilder.build().toString();

      // Circuit breaker scenario
      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs(Scenario.STARTED)
            .willSetStateTo("timeout-1")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody(productJson)
            ));

      // Da un timeout 1 vez
      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs("timeout-1")
            .willSetStateTo("timeout-2")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(200)
            ));

      // Da un timeout 2 vez
      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs("timeout-2")
            .willSetStateTo("success")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withFixedDelay(200)
            ));

      stubFor(get(urlEqualTo("/products/circuitBreaker"))
            .inScenario("circuitBreaker")
            .whenScenarioStateIs("success")
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody(productJson)
            ));
   }

   static void stubSecured() {
      JsonObjectBuilder objectBuilder = Json.createObjectBuilder();
      objectBuilder
            .add("sku", "secured")
            .add("productLine", "DELUXE")
            .add("name", "SECURED")
            .add("price", 12.8);

      stubFor(get(urlEqualTo("/products/secured"))
            .willReturn(aResponse()
                  .withHeader("Content-Type", "application/json")
                  .withBody(objectBuilder.build().toString())
            ));
   }

   @Override
   public void stop() {
      if (wireMockServer != null) {
         wireMockServer.stop();
      }
   }
}
