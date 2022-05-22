package com.kineteco.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.ws.rs.NotFoundException;
import java.util.Optional;

@RegisterForReflection
@Entity
public class Customer extends PanacheEntity {
   public String customerId;
   public String name;
   public String email;

   public static Optional<Customer> findByCustomerId(String customerId) {
      return find("customerId", customerId).firstResultOptional();
   }
}
