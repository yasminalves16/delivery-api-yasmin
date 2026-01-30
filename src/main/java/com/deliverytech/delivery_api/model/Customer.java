package com.deliverytech.delivery_api.model;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customers")
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Customer {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private String name;

  @Column(unique = true, nullable = false)
  private String email;

  private String phone;

  private String address;

  @Column(name = "created_at")
  private LocalDateTime createdAt;

  private Boolean active;

  @OneToMany(mappedBy = "customer")
  @JsonIgnore
  private List<CustomerOrder> orders = new ArrayList<>();

  @PrePersist
  public void PrePersist(){
    this.createdAt = LocalDateTime.now();
  }

}
