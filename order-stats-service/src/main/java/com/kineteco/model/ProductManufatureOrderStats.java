package com.kineteco.model;

public class ProductManufatureOrderStats {
   public String sku;
   public String category;
   public String name;
   public int min = Integer.MAX_VALUE;
   public int max = Integer.MIN_VALUE;
   public int count;
   public int sum;
   public int avg;

   public ProductManufatureOrderStats updateFrom(ProductManufatureOrder productManufatureOrder) {
      sku = productManufatureOrder.sku;
      name = productManufatureOrder.name;
      category = productManufatureOrder.category;
      min = Math.min(min, productManufatureOrder.quantity);
      max = Math.max(max, productManufatureOrder.quantity);
      count++;
      sum += productManufatureOrder.quantity;
      avg = Math.round(sum / count);

      return this;
   }
}
