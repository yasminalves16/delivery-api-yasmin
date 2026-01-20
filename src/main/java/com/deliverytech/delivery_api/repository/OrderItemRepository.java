package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.model.OrderItem;

public interface OrderItemRepository extends JpaRepository<OrderItem, Long> {

  List<OrderItem> findByOrderId(Long orderId);

  List<OrderItem> findByProductId(Long productId);
}
