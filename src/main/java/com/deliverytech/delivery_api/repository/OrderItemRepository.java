package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  List<OrderItem> findByCustomerOrderId(Long customerOrderId);

  List<OrderItem> findByProductId(Long productId);
}
