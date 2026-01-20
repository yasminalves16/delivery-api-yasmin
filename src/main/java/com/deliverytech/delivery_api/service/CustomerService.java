package com.deliverytech.delivery_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.model.Customer;
import com.deliverytech.delivery_api.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
  private CustomerRepository customerRepository;

  public CustomerService(CustomerRepository customerRepository) {
    this.customerRepository = customerRepository;
  }

  public Customer registerCustomer(Customer customer) {
    if (customerRepository.existsByEmail(customer.getEmail())) {
      throw new IllegalArgumentException("Email já está em uso.");
    }
    customer.setActive(true);
    customer.setCreatedAt(LocalDateTime.now());
    return customerRepository.save(customer);
  }

  public List<Customer> getActiveCustomers() {
    return customerRepository.findByActiveTrue();
  }

  public List<Customer> searchCustomersByName(String name) {
    return customerRepository.findByNameContainingIgnoreCase(name);
  }

  public Customer searchCustomerById(Long id) {
    return customerRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Cliente não encontrado "));
  }

  public Customer updateCustomer(Long id, Customer updatedCustomer) {
    Customer existingCustomer = searchCustomerById(id);

    existingCustomer.setName(updatedCustomer.getName());
    existingCustomer.setEmail(updatedCustomer.getEmail());
    existingCustomer.setPhoneNumber(updatedCustomer.getPhoneNumber());
    existingCustomer.setAddress(updatedCustomer.getAddress());

    return customerRepository.save(existingCustomer);
  }

  public void deactivateCustomer(Long id) {
    Customer existingCustomer = searchCustomerById(id);
    existingCustomer.setActive(false);
    customerRepository.save(existingCustomer);
  }
}
