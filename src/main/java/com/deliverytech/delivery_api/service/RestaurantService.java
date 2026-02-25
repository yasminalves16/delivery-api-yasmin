package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.deliverytech.delivery_api.dto.responses.RestaurantResponseDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.RestaurantRepository;
import com.deliverytech.delivery_api.repository.UserRepository;

@Service
public class RestaurantService {
  private RestaurantRepository restaurantRepository;
  private UserRepository userRepository;
  private final ModelMapper mapper;

  public RestaurantService(RestaurantRepository restaurantRepository, UserRepository userRepository,
      ModelMapper mapper) {
    this.restaurantRepository = restaurantRepository;
    this.userRepository = userRepository;
    this.mapper = mapper;
  }

  private RestaurantResponseDTO toRestaurantResponseDTO(Restaurant restaurant) {
    RestaurantResponseDTO dto = mapper.map(restaurant, RestaurantResponseDTO.class);
    if (restaurant.getUser() != null) {
      dto.setUserId(restaurant.getUser().getId());
      dto.setUserName(restaurant.getUser().getName());
      dto.setUserEmail(restaurant.getUser().getEmail());
    }
    return dto;
  }

  @Transactional
  public RestaurantResponseDTO registerRestaurant(RestaurantDTO dto) {

    String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByEmail(loggedEmail)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

    User targetRestaurantUser;

    if (user.getRole() == Role.ADMIN) {
      if (dto.getUserId() == null) {
        throw new BusinessException(
            "Admin deve informar o userId de um usuário RESTAURANT para vincular ao restaurante.");
      }

      targetRestaurantUser = userRepository.findById(dto.getUserId())
          .orElseThrow(() -> new EntityNotFoundException("Usuário RESTAURANT informado não encontrado."));

      if (targetRestaurantUser.getRole() != Role.RESTAURANT) {
        throw new BusinessException("O usuário designado deve ter perfil RESTAURANT.");
      }
    } else if (user.getRole() == Role.RESTAURANT) {
      targetRestaurantUser = user;
    } else {
      throw new BusinessException("Somente ADMIN ou usuários com perfil RESTAURANT podem criar restaurante.");
    }

    if (restaurantRepository.existsByUserId(targetRestaurantUser.getId())) {
      throw new BusinessException("Este usuário restaurante já está associado a um restaurante cadastrado.");
    }

    if (restaurantRepository.existsByName(dto.getName())) {
      throw new BusinessException("Já existe um restaurante cadastrado com esse nome");
    }

    Restaurant restaurant = mapper.map(dto, Restaurant.class);

    restaurant.setActive(true);
    restaurant.setRating(BigDecimal.ZERO);
    restaurant.setUser(targetRestaurantUser);
    targetRestaurantUser.setRestaurant(restaurant);

    Restaurant savedRestaurant = restaurantRepository.save(restaurant);
    targetRestaurantUser.setRestaurantId(savedRestaurant.getId());
    userRepository.save(targetRestaurantUser);

    return toRestaurantResponseDTO(savedRestaurant);
  }

  @Transactional(readOnly = true)
  public Page<RestaurantResponseDTO> getActiveRestaurants(Pageable pageable) {
    return restaurantRepository.findByActiveTrue(pageable)
        .map(this::toRestaurantResponseDTO);
  }

  @Transactional(readOnly = true)
  public List<RestaurantResponseDTO> getAllRestaurants() {
    return restaurantRepository.findAll()
        .stream()
        .map(this::toRestaurantResponseDTO)
        .toList();
  }

  @Transactional(readOnly = true)
  public List<RestaurantResponseDTO> searchRestaurantByCategory(String category) {
    return restaurantRepository.findByCategory(category)
        .stream()
        .map(this::toRestaurantResponseDTO)
        .toList();
  }

  @Transactional(readOnly = true)
  public RestaurantResponseDTO searchRestaurantById(Long id) {
    Restaurant restaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));
    return toRestaurantResponseDTO(restaurant);
  }

  @Transactional
  public RestaurantResponseDTO toggleRestaurantActive(Long id) {
    Restaurant existingRestaurant = restaurantRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

    String loggedEmail = SecurityContextHolder.getContext().getAuthentication().getName();
    User user = userRepository.findByEmail(loggedEmail)
        .orElseThrow(() -> new EntityNotFoundException("Usuário não encontrado"));

    if (user.getRole() == Role.RESTAURANT) {
      Long userRestaurantId = user.getRestaurantId();
      if (userRestaurantId == null && user.getRestaurant() != null) {
        userRestaurantId = user.getRestaurant().getId();
      }

      if (userRestaurantId == null || !userRestaurantId.equals(existingRestaurant.getId())) {
        throw new BusinessException("Usuário RESTAURANT só pode alterar o próprio restaurante.");
      }
    }

    existingRestaurant.setActive(!existingRestaurant.getActive());
    restaurantRepository.save(existingRestaurant);
    return toRestaurantResponseDTO(existingRestaurant);
  }

}
