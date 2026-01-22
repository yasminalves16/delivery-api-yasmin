package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.enums.CustomerOrderStatus;
import com.deliverytech.delivery_api.model.CustomerOrder;
import com.deliverytech.delivery_api.service.CustomerOrderService;

@RestController
@RequestMapping("/customer-orders")
public class CustomerOrderController {
  private final CustomerOrderService customerOrderService;

  public CustomerOrderController(CustomerOrderService customerOrderService) {
    this.customerOrderService = customerOrderService;
  }

  @PostMapping
  public CustomerOrder createOrder(@RequestParam Long customerId, @RequestParam Long restaurantId) {
    return customerOrderService.createOrder(customerId, restaurantId);
  }

  @PutMapping("/{id}/status")
  public CustomerOrder updateOrderStatus(@PathVariable Long orderId, @RequestParam CustomerOrderStatus status) {
    return customerOrderService.updateStatusOrder(orderId, status);
  }

  @GetMapping("/customer/{id}")
  public List<CustomerOrder> getOrdersByCustomerId(@PathVariable("id") Long customerId) {
    return customerOrderService.getOrdersByCustomerId(customerId);
  }

  @GetMapping
  public List<CustomerOrder> getAllOrders() {
    return customerOrderService.getAllOrders();
  }

  @GetMapping("/{id}")
  public CustomerOrder getOrderById(@PathVariable Long id) {
    return customerOrderService.getOrderById(id);
  }
}
