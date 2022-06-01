package com.kineteco.model;

import io.quarkus.runtime.annotations.RegisterForReflection;

@RegisterForReflection
public class ProductManufatureOrder {
   public String sku;
   public String category;
   public String name;
   public int unitsAvailable;
   public int quantity;

   @Override
   public String toString() {
      return "ProductManufatureOrder{" + "sku='" + sku + '\'' + ", category='" + category + '\'' + ", name='" + name
            + '\'' + ", unitsAvailable=" + unitsAvailable + ", quantity=" + quantity + '}';
   }
}
