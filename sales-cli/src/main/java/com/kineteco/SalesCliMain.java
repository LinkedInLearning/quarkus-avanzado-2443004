package com.kineteco;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.annotations.QuarkusMain;

@QuarkusMain
public class SalesCliMain {
   public static void main(String[] args) {
      Quarkus.run(SalesCli.class, args);
   }
}
