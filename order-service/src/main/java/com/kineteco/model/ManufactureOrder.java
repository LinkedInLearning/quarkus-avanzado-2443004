package com.kineteco.model;

public class ManufactureOrder {
   public String sku;
   public int quantity;

   @Override
   public String toString() {
      return "ManufactureOrder{" + "sku='" + sku + '\'' + ", quantity=" + quantity + '}';
   }
}
