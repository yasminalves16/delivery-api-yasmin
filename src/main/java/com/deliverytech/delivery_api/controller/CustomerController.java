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

import com.deliverytech.delivery_api.model.Customer;
import com.deliverytech.delivery_api.service.CustomerService;

@RestController
@RequestMapping("/customers")
public class CustomerController {
  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  public ResponseEntity<Customer> registerCustomer(@RequestBody Customer customer) {
    return ResponseEntity.status(201).body(customerService.registerCustomer(customer));
  }

  @GetMapping
  public List<Customer> getActiveCustomers() {
    return customerService.getActiveCustomers();
  }
  //faltou a busca por nome
  
  @GetMapping("/{id}")
  public Customer searchCustomerById(@PathVariable Long id) {
    return customerService.searchCustomerById(id);
  }

  @PutMapping("/{id}")
  public Customer updateCustomer(@PathVariable Long id, @RequestBody Customer updatedCustomer) {
    return customerService.updateCustomer(id, updatedCustomer);
  }

  @PutMapping("/deactivate/{id}")
  public ResponseEntity<Void> deactivateCustomer(@PathVariable Long id) {
    customerService.deactivateCustomer(id);
    return ResponseEntity.noContent().build();
  }
}
