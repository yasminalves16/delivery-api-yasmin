package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.CustomerOrderDTO;
import com.deliverytech.delivery_api.dto.responses.CustomerOrderResponseDTO;
import com.deliverytech.delivery_api.service.CustomerOrderService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customer-orders")
public class CustomerOrderController {
  private final CustomerOrderService customerOrderService;

  public CustomerOrderController(CustomerOrderService customerOrderService) {
    this.customerOrderService = customerOrderService;
  }

  @PostMapping
  public ResponseEntity<CustomerOrderResponseDTO> createOrder(@RequestBody @Valid CustomerOrderDTO customerOrderDTO) {
    return ResponseEntity.ok(customerOrderService.createOrder(customerOrderDTO));
  }

  @PutMapping("/{orderId}/confirm")
  public ResponseEntity<CustomerOrderResponseDTO> confirmOrder(@PathVariable Long orderId) {
    return ResponseEntity.ok(customerOrderService.confirmOrder(orderId));
  }

  @PutMapping("/{orderId}/cancel")
  public ResponseEntity<CustomerOrderResponseDTO> cancelOrder(@PathVariable Long orderId) {
    return ResponseEntity.ok(customerOrderService.cancelOrder(orderId));
  }

  @PutMapping("/{orderId}/status")
  public ResponseEntity<CustomerOrderResponseDTO> updateOrderStatus(@PathVariable Long orderId) {
    return ResponseEntity.ok(customerOrderService.updateStatusOrder(orderId));
  }

  @GetMapping("/customer/{customerId}")
  public ResponseEntity<List<CustomerOrderResponseDTO>> getOrdersByCustomerId(
      @PathVariable("customerId") Long customerId) {
    return ResponseEntity.ok(customerOrderService.getOrdersByCustomerId(customerId));
  }

  @GetMapping
  public ResponseEntity<List<CustomerOrderResponseDTO>> getAllOrders() {
    return ResponseEntity.ok(customerOrderService.getAllOrders());
  }

  @GetMapping("/{orderId}")
  public ResponseEntity<CustomerOrderResponseDTO> getOrderById(@PathVariable Long orderId) {
    return ResponseEntity.ok(customerOrderService.getOrderById(orderId));
  }
}
