package com.deliverytech.delivery_api.model;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "order_items")
public class OrderItem {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  private Integer quantity;

  @Column(name = "unit_price")
  private BigDecimal unitPrice;

  private BigDecimal subtotal;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "product_id")
  @JsonIgnore
  private Product product;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_order_id")
  @JsonIgnore
  private CustomerOrder customerOrder;

}
