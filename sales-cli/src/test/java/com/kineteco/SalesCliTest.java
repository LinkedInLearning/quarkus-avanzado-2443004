package com.kineteco;

import io.quarkus.test.junit.main.Launch;
import io.quarkus.test.junit.main.LaunchResult;
import io.quarkus.test.junit.main.QuarkusMainTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

@QuarkusMainTest
public class SalesCliTest {
   @Test
   @Launch("view")
   public void testLaunchDefaultView(LaunchResult result) {
      Assertions.assertTrue(result.getOutput().contains("email='penatibus.et@lectusa.com"));
   }

   @Test
   @Launch(value = { "view", "--id=2" } )
   public void testLaunchIdArgument(LaunchResult result) {
      Assertions.assertTrue(result.getOutput().contains("email='nibh@ultricesposuere.edu'"));
   }
}
