package com.deliverytech.delivery_api.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "restaurants")
public class Restaurant {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  private String category;

  private String address;

  private String phone;

  private BigDecimal rating;

  @Column(name = "delivery_fee")
  private BigDecimal deliveryFee;

  private Boolean active;

  @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<Product> products = new ArrayList<>();

  @OneToMany(mappedBy = "restaurant", fetch = FetchType.LAZY)
  @JsonIgnore
  private List<CustomerOrder> orders = new ArrayList<>();

}
