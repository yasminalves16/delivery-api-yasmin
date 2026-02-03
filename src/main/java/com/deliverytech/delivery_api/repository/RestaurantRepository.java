package com.deliverytech.delivery_api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.deliverytech.delivery_api.model.Restaurant;

@Repository
public interface RestaurantRepository extends JpaRepository<Restaurant, Long> {

  List<Restaurant> findByActiveTrue();

  List<Restaurant> findByCategory(String category);

  List<Restaurant> findByActiveIsTrueOrderByRatingDesc();

  List<Restaurant> findByNameContainingIgnoreCase(String name);

  List<Restaurant> findByActiveTrueAndCategory(String category);

  boolean existsByName(String name);

}
