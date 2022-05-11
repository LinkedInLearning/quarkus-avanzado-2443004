package com.kineteco.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;

import javax.persistence.Entity;

@Entity
public class Customer extends PanacheEntity {
   public String customerId;
   public String name;
   public String email;

   @Override
   public String toString() {
      return "Customer{" + "customerId='" + customerId + '\'' + ", name='" + name + '\'' + ", email='" + email + '\''
            + '}';
   }
}
