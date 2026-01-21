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

import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.service.RestaurantService;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
  private final RestaurantService restaurantService;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @PostMapping
  public ResponseEntity<Restaurant> registerRestaurant(@RequestBody Restaurant restaurant) {
    return ResponseEntity.status(201).body(restaurantService.registerRestaurant(restaurant));
  }

  @GetMapping
  public List<Restaurant> getActiveRestaurants() {
    return restaurantService.getActiveRestaurants();
  }

  @GetMapping("/{id}")
  public Restaurant searchRestaurantById(@PathVariable Long id) {
    return restaurantService.searchRestaurantById(id);
  }

  @PutMapping("/deactivate/{id}")
  public ResponseEntity<Void> deactivateRestaurant(@PathVariable Long id) {
    restaurantService.deactivateRestaurant(id);
    return ResponseEntity.noContent().build();
  }

}
