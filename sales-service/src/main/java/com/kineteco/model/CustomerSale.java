package com.kineteco.model;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import io.quarkus.runtime.annotations.RegisterForReflection;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.util.List;

@RegisterForReflection
@Entity
public class CustomerSale extends PanacheEntity {
   @OneToOne
   public Customer customer;

   @OneToMany
   public List<ProductSale> productSale;
}
