package com.deliverytech.delivery_api.service;

import java.time.LocalDateTime;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.CustomerDTO;
import com.deliverytech.delivery_api.dto.responses.CustomerResponseDTO;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Customer;
import com.deliverytech.delivery_api.repository.CustomerRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
public class CustomerService {
    private CustomerRepository customerRepository;
    private final ModelMapper mapper;

    public CustomerService(CustomerRepository customerRepository, ModelMapper mapper) {
        this.customerRepository = customerRepository;
        this.mapper = mapper;
    }

    public CustomerResponseDTO registerCustomer(CustomerDTO dto) {
        if (customerRepository.existsByEmail(dto.getEmail())) {
            throw new BusinessException("Email já está em uso.");
        }

        Customer customer = mapper.map(dto, Customer.class);
        customer.setActive(true);
        customer.setCreatedAt(LocalDateTime.now());
        Customer savedCustomer = customerRepository.save(customer);
        return mapper.map(savedCustomer, CustomerResponseDTO.class);
    }

    public List<CustomerResponseDTO> getActiveCustomers() {
        return customerRepository.findByActiveTrue()
                .stream()
                .map(customer -> mapper.map(customer, CustomerResponseDTO.class))
                .toList();
    }

    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(customer -> mapper.map(customer, CustomerResponseDTO.class))
                .toList();
    }
    
    public List<CustomerResponseDTO> searchCustomersByName(String name) {
        return customerRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(customer -> mapper.map(customer, CustomerResponseDTO.class))
                .toList();
    }

    public CustomerResponseDTO searchCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        return mapper.map(customer, CustomerResponseDTO.class);
    }

    // public Customer updateCustomer(Long id, Customer updatedCustomer) {
    // Customer existingCustomer = searchCustomerById(id);

    // existingCustomer.setName(updatedCustomer.getName());
    // existingCustomer.setEmail(updatedCustomer.getEmail());
    // existingCustomer.setPhone(updatedCustomer.getPhone());
    // existingCustomer.setAddress(updatedCustomer.getAddress());

    // return customerRepository.save(existingCustomer);
    // }

    @Transactional
    public CustomerResponseDTO toggleCustomerActive(Long id) {
        Customer existingCustomer = customerRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Cliente não encontrado"));
        existingCustomer.setActive(!existingCustomer.getActive());
        return mapper.map(existingCustomer, CustomerResponseDTO.class);
    }
}
