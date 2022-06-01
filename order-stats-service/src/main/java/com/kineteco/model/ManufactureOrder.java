package com.kineteco.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ManufactureOrder {
   public String sku;
   public int quantity;

   @Override
   public String toString() {
      return "ManufactureOrder{" + "sku='" + sku + '\'' + ", quantity=" + quantity + '}';
   }
}
