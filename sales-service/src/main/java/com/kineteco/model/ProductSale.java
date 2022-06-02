package com.kineteco.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import java.math.BigDecimal;

@RegisterForReflection
@Entity
public class ProductSale extends PanacheEntity {

   public String sku;
   public String name;
   public BigDecimal total;

   @Override
   public String toString() {
      return "ProductSale{" + "sku='" + sku + '\'' + ", name='" + name + '\'' + ", total=" + total + '}';
   }
}