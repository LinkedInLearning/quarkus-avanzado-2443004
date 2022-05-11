package com.kineteco;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusMainTest
public class SalesCliTest {

   @Test
   @Launch("World")
   public void testLaunchCommand(LaunchResult result) {
      Assertions.assertTrue(result.getOutput().contains("sales-cli 1.0.0-SNAPSHOT on JVM"));
   }
}
