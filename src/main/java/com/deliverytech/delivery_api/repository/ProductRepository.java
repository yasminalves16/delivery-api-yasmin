package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.deliverytech.delivery_api.model.Product;

public interface ProductRepository extends JpaRepository<Product, Long> {
  List<Product> findByRestaurantId(Long restaurantId);

  List<Product> findByCategory(String category);

  List<Product> findByAvailableTrue();

  List<Product> findByRestaurantIdAndAvailableTrue(Long restaurantId);
}
