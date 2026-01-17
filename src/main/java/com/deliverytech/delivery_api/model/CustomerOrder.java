package com.deliverytech.delivery_api.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import com.deliverytech.delivery_api.enums.CustomerOrderStatus;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "customer_orders")
public class CustomerOrder {
  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(name = "date_order")
  private LocalDateTime dateOrder;

  @Column(name = "address_delivery")
  private String addressDelivery;

  @Column(name = "order_number")
  private String orderNumber;

  @Column(name = "delivery_fee")
  private BigDecimal deliveryFee;

  @Column(name = "total_value")
  private BigDecimal totalValue;

  @Enumerated(EnumType.STRING)
  private CustomerOrderStatus status;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "customer_id")
  private Customer customer;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "restaurant_id")
  private Restaurant restaurant;

  @OneToMany(mappedBy = "customerOrder")
  private java.util.List<OrderItem> orderItems;

}
