package com.kineteco.model;

public class OrderStat {
   public String sku;
   public int count;

   @Override
   public String toString() {
      return "OrderStat{" + "sku='" + sku + '\'' + ", count=" + count + '}';
   }
}
