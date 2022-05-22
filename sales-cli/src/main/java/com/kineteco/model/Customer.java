package com.kineteco.model;

public class Customer {
   public Long id;
   public String customerId;
   public String name;
   public String email;

   @Override
   public String toString() {
      return "Customer{" + "id=" + id + ", customerId='" + customerId + '\'' + ", name='" + name + '\'' + ", email='"
            + email + '\'' + '}';
   }
}
