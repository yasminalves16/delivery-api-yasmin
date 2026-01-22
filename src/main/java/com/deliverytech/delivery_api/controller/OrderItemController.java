package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.model.OrderItem;
import com.deliverytech.delivery_api.service.OrderItemService;

@RestController
@RequestMapping("/order-items")
public class OrderItemController {

  private final OrderItemService orderItemService;

  public OrderItemController(OrderItemService orderItemService) {
    this.orderItemService = orderItemService;
  }

  @GetMapping("/order/{orderId}")
  public ResponseEntity<List<OrderItem>> getByOrder(@PathVariable Long orderId) {
    return ResponseEntity.ok(orderItemService.getOrderItemsByOrderId(orderId));
  }

  @GetMapping("/{id}")
  public ResponseEntity<OrderItem> getById(@PathVariable Long id) {
    return ResponseEntity.ok(orderItemService.getOrderItemById(id));
  }

  @PutMapping("/{id}")
  public ResponseEntity<OrderItem> updateQuantity(
      @PathVariable Long id,
      @RequestParam Integer quantity) {

    return ResponseEntity.ok(orderItemService.updateOrderItem(id, quantity));
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> delete(@PathVariable Long id) {
    orderItemService.removeOrderItem(id);
    return ResponseEntity.noContent().build();
  }

  @GetMapping("/product/{productId}")
  public ResponseEntity<List<OrderItem>> getByProduct(@PathVariable Long productId) {
    return ResponseEntity.ok(orderItemService.getOrderItemsByProductId(productId));
  }

  @GetMapping
  public ResponseEntity<List<OrderItem>> getAll() {
    return ResponseEntity.ok(orderItemService.getAllOrderItems());
  }
}
