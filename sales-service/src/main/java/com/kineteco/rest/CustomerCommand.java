package com.kineteco.rest;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.util.Objects;

public class CustomerCommand {
   @NotNull
   private String customerId;
   @NotNull
   private String sku;
   @Positive
   private Integer units;

   public String getCustomerId() {
      return customerId;
   }

   public void setCustomerId(String customerId) {
      this.customerId = customerId;
   }

   public String getSku() {
      return sku;
   }

   public void setSku(String sku) {
      this.sku = sku;
   }

   public Integer getUnits() {
      return units;
   }

   public void setUnits(Integer units) {
      this.units = units;
   }

   @Override
   public boolean equals(Object o) {
      if (this == o)
         return true;
      if (o == null || getClass() != o.getClass())
         return false;
      CustomerCommand that = (CustomerCommand) o;
      return Objects.equals(customerId, that.customerId) && Objects.equals(sku, that.sku) && Objects.equals(units,
            that.units);
   }

   @Override
   public int hashCode() {
      return Objects.hash(customerId, sku, units);
   }

   @Override
   public String toString() {
      return "CustomerCommand{" + "customerId='" + customerId + '\'' + ", sku='" + sku + '\'' + ", units=" + units
            + '}';
   }
}
