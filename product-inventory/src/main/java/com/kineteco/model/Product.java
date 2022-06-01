package com.kineteco.model;

public class Product {
   public String sku;
   public String category;
   public String name;
   public int unitsAvailable;

   @Override
   public String toString() {
      return "Product{" + "sku='" + sku + '\'' + ", category='" + category + '\'' + ", name='" + name + '\''
            + ", unitsAvailable=" + unitsAvailable + '}';
   }
}
