package com.kineteco.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;
import org.infinispan.protostream.annotations.ProtoFactory;
import org.infinispan.protostream.annotations.ProtoField;

import javax.persistence.Entity;
import java.util.Optional;

@RegisterForReflection
@Entity
public class Customer extends PanacheEntity {
   @ProtoField(1)
   public String name;
   @ProtoField(2)
   public String customerId;
   @ProtoField(3)
   public String email;

   public Customer() {
   }

   @ProtoFactory
   public Customer(String customerId, String name, String email) {
      this.customerId = customerId;
      this.name = name;
      this.email = email;
   }

   public static Optional<Customer> findByCustomerId(String customerId) {
      return find("customerId", customerId).firstResultOptional();
   }
}
