package com.kineteco.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import java.math.BigDecimal;
import java.util.Objects;

@RegisterForReflection
@Entity
public class ProductSale extends PanacheEntity {

   public String sku;
   public String name;
   public BigDecimal total;

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      ProductSale that = (ProductSale) o;
      return Objects.equals(sku, that.sku) && Objects.equals(name, that.name) && Objects.equals(total, that.total);
   }

   @Override
   public int hashCode() {
      return Objects.hash(sku, name, total);
   }

   @Override
   public String toString() {
      return "ProductSale{" + "sku='" + sku + '\'' + ", name='" + name + '\'' + ", total=" + total + '}';
   }
}