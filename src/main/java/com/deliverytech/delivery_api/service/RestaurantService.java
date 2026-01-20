package com.deliverytech.delivery_api.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

@Service
public class RestaurantService {
  private RestaurantRepository restaurantRepository;

  public RestaurantService(RestaurantRepository restaurantRepository) {
    this.restaurantRepository = restaurantRepository;
  }

  public Restaurant registerRestaurant(Restaurant restaurant) {
    restaurant.setActive(true);
    return restaurantRepository.save(restaurant);
  }

  public List<Restaurant> getActiveRestaurants() {
    return restaurantRepository.findByActiveTrue();
  }

  public List<Restaurant> searchRestaurantByCategory(String category) {
    return restaurantRepository.findByCategory(category);
  }

  public Restaurant searchRestaurantById(Long id) {
    return restaurantRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
  }

  public void deactivateRestaurant(Long id) {
    Restaurant existingRestaurant = searchRestaurantById(id);
    existingRestaurant.setActive(false);
    restaurantRepository.save(existingRestaurant);
  }

}
