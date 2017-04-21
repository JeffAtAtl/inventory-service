package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Sku {

  //@OneToMany(mappedBy = "sku")
  //private Set<Store> stores = new HashSet<>();

  @Id
  @GeneratedValue
  public Long id;
  
  public Long getId() {
		return id;
	}

  //public Set<Store> getStores() {
  //  return stores;
  //}

  public String getSku() {
    return sku;
  }

  public String getDescription() {
    return description;
  }

  public String sku;
  public String description;

  public Sku(String sku, String description) {
    this.sku = sku;
    this.description = description;
  }

  Sku() { // jpa only
  }

}
