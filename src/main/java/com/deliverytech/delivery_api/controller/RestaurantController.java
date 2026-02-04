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

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.deliverytech.delivery_api.dto.responses.RestaurantResponseDTO;
import com.deliverytech.delivery_api.service.RestaurantService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/restaurants")
public class RestaurantController {
  private final RestaurantService restaurantService;

  public RestaurantController(RestaurantService restaurantService) {
    this.restaurantService = restaurantService;
  }

  @PostMapping
  public ResponseEntity<RestaurantResponseDTO> registerRestaurant(@RequestBody @Valid RestaurantDTO restaurant) {
    RestaurantResponseDTO responseDTO = restaurantService.registerRestaurant(restaurant);
    return ResponseEntity.status(HttpStatus.CREATED).body(responseDTO);
  }

  @GetMapping
  public ResponseEntity<List<RestaurantResponseDTO>> getActiveRestaurants() {
    return ResponseEntity.ok(restaurantService.getActiveRestaurants());
  }

  @GetMapping("/all")
  public ResponseEntity<List<RestaurantResponseDTO>> getAllRestaurants() {
    return ResponseEntity.ok(restaurantService.getAllRestaurants());
  }

  @GetMapping("/{id}")
  public ResponseEntity<RestaurantResponseDTO> searchRestaurantById(@PathVariable Long id) {
    return ResponseEntity.ok(restaurantService.searchRestaurantById(id));
  }

  @GetMapping("/category")
  public List<RestaurantResponseDTO> searchRestaurantByCategory(@RequestParam String category) {
    return restaurantService.searchRestaurantByCategory(category);
  }

  @PatchMapping("/{id}/toggle")
  public RestaurantResponseDTO toggleRestaurantActive(@PathVariable Long id) {
    return restaurantService.toggleRestaurantActive(id);
  }

}
