package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.CustomerDTO;
import com.deliverytech.delivery_api.dto.responses.CustomerResponseDTO;
import com.deliverytech.delivery_api.service.CustomerService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
public class CustomerController {
  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @PostMapping
  public ResponseEntity<CustomerResponseDTO> registerCustomer(@Valid @RequestBody CustomerDTO customer) {
    return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(customer));
  }

  @GetMapping
  public List<CustomerResponseDTO> getActiveCustomers() {
    return customerService.getActiveCustomers();
  }

  @GetMapping("/all")
  public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
    List<CustomerResponseDTO> allCustomers = customerService.getAllCustomers();
    return ResponseEntity.ok(allCustomers);
  }

  @GetMapping("/search")
  public List<CustomerResponseDTO> searchCustomersByName(@RequestParam String name) {
    return customerService.searchCustomersByName(name);
  }

  @GetMapping("/{id}")
  public CustomerResponseDTO searchCustomerById(@PathVariable Long id) {
    return customerService.searchCustomerById(id);
  }

  // @PutMapping("/{id}")
  // public CustomerResponseDTO updateCustomer(@PathVariable Long id, @RequestBody
  // Customer updatedCustomer) {
  // return customerService.updateCustomer(id, updatedCustomer);
  // }

  @PatchMapping("/{id}/toggle")
  public CustomerResponseDTO toggleCustomerActive(@PathVariable Long id) {
    return customerService.toggleCustomerActive(id);
  }
}
