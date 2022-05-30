package com.kineteco.model;

import java.util.Objects;

public class OrderStat {
   public String sku;
   public int count;

   @Override
   public String toString() {
      return "OrderStat{" + "sku='" + sku + '\'' + ", count=" + count + '}';
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      OrderStat stat = (OrderStat) o;
      return Objects.equals(sku, stat.sku);
   }

   @Override
   public int hashCode() {
      return Objects.hash(sku);
   }
}
