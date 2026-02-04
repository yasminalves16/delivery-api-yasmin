package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.deliverytech.delivery_api.dto.responses.RestaurantResponseDTO;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
public class RestaurantService {
  private RestaurantRepository restaurantRepository;
  private final ModelMapper mapper;

  public RestaurantService(RestaurantRepository restaurantRepository, ModelMapper mapper) {
    this.restaurantRepository = restaurantRepository;
    this.mapper = mapper;
  }

  @Transactional
  public RestaurantResponseDTO registerRestaurant(RestaurantDTO dto) {
    if (restaurantRepository.existsByName(dto.getName())) {
      throw new BusinessException("Já existe um restaurante cadastrado com esse nome");
    }

    Restaurant restaurant = mapper.map(dto, Restaurant.class);

    restaurant.setActive(true);
    restaurant.setRating(BigDecimal.ZERO);

    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
    return mapper.map(savedRestaurant, RestaurantResponseDTO.class);

  }

  public List<RestaurantResponseDTO> getActiveRestaurants() {
    return restaurantRepository.findByActiveTrue()
        .stream()
        .map(restaurant -> mapper.map(restaurant, RestaurantResponseDTO.class))
        .toList();
  }

  public List<RestaurantResponseDTO> getAllRestaurants() {
    return restaurantRepository.findAll()
        .stream()
        .map(restaurant -> mapper.map(restaurant, RestaurantResponseDTO.class))
        .toList();
  }

  public List<RestaurantResponseDTO> searchRestaurantByCategory(String category) {
    return restaurantRepository.findByCategory(category)
        .stream()
        .map(restaurant -> mapper.map(restaurant, RestaurantResponseDTO.class))
        .toList();
  }

  public RestaurantResponseDTO searchRestaurantById(Long id) {
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
    return mapper.map(restaurant, RestaurantResponseDTO.class);
  }

  @Transactional
  public RestaurantResponseDTO toggleRestaurantActive(Long id) {
    Restaurant existingRestaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
    existingRestaurant.setActive(!existingRestaurant.getActive());
    restaurantRepository.save(existingRestaurant);
    return mapper.map(existingRestaurant, RestaurantResponseDTO.class);
  }

}
