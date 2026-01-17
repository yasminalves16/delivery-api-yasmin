package com.deliverytech.delivery_api.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Customer;

@Repository
public interface ClientRepository extends JpaRepository<Customer, Long> {

  Optional<Customer> findByEmail(String email);

  List<Customer> findByActiveTrue();

}
