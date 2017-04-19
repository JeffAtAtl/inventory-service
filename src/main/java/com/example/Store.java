package com.example;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

@Entity
public class Store {

  @JsonIgnore
  @ManyToOne
  private Sku sku;

  @Id
  @GeneratedValue
  private Long id;

  Store() { // jpa only
  }

  public String storeNo;
  public Integer count;

  public Long getId() {
	return id;
}

public Sku getSku() {
    return sku;
  }

  public String getStoreNo() {
    return storeNo;
  }

  public Integer getCount() {
    return count;
  }

  public Store(Sku sku, String storeNo, Integer count) {
    this.storeNo = storeNo;
    this.count = count;
    this.sku = sku;
  }
}
