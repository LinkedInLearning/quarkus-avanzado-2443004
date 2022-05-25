package com.kineteco.client;

import java.math.BigDecimal;
import java.util.Objects;

public class Product {
   private String sku;
   private String name;
   private String productLine;
   private BigDecimal price;

   public Product() {
   }

   public String getSku() {
      return sku;
   }

   public String getProductLine() {
      return productLine;
   }

   public BigDecimal getPrice() {
      return price;
   }

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public void setProductLine(String productLine) {
      this.productLine = productLine;
   }

   public void setPrice(BigDecimal price) {
      this.price = price;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      Product product = (Product) o;
      return Objects.equals(sku, product.sku) && Objects.equals(name, product.name) && Objects.equals(productLine,
            product.productLine) && Objects.equals(price, product.price);
   }

   @Override
   public int hashCode() {
      return Objects.hash(sku, name, productLine, price);
   }

   @Override
   public String toString() {
      return "Product{" + "sku='" + sku + '\'' + ", name='" + name + '\'' + ", productLine='" + productLine + '\''
            + ", price=" + price + '}';
   }
}