package com.deliverytech.delivery_api.service;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.dto.requests.ProductDTO;
import com.deliverytech.delivery_api.dto.responses.ProductResponseDTO;
import com.deliverytech.delivery_api.exceptions.BusinessException;
import com.deliverytech.delivery_api.exceptions.EntityNotFoundException;
import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

import jakarta.transaction.Transactional;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final RestaurantRepository restaurantRepository;
  private final ModelMapper mapper;

  public ProductService(ProductRepository productRepository, RestaurantRepository restaurantRepository,
      ModelMapper mapper) {
    this.productRepository = productRepository;
    this.restaurantRepository = restaurantRepository;
    this.mapper = mapper;
  }

  @Transactional
  public ProductResponseDTO registerProduct(Long restaurantId, ProductDTO product) {
    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new EntityNotFoundException("Restaurante não encontrado"));

    if (!restaurant.getActive()) {
      throw new BusinessException("Não é possível adicionar produtos a um restaurante inativo");
    }

    Product newProduct = mapper.map(product, Product.class);
    newProduct.setAvailable(true);
    newProduct.setRestaurant(restaurant);
    Product savedProduct = productRepository.save(newProduct);

    ProductResponseDTO response = mapper.map(savedProduct, ProductResponseDTO.class);
    response.setRestaurantId(restaurant.getId());
    return response;
  }

  @Transactional
  public List<ProductResponseDTO> getProductsByRestaurantId(Long restaurantId) {
    // return productRepository.findByRestaurantId(restaurantId);
    if (!restaurantRepository.existsById(restaurantId)) {
      throw new EntityNotFoundException("Restaurante não encontrado");
    }

    return productRepository.findByRestaurantIdAndAvailableTrue(restaurantId)
        .stream()
        .map(product -> {
          Long restId = product.getRestaurant().getId();
          ProductResponseDTO dto = mapper.map(product, ProductResponseDTO.class);
          dto.setRestaurantId(restId);
          return dto;
        })
        .toList();
  }

  @Transactional
  public List<ProductResponseDTO> getAllAvailableProducts() {
    return productRepository.findByAvailableTrue()
        .stream()
        .map(product -> {
          Long restId = product.getRestaurant().getId();
          ProductResponseDTO dto = mapper.map(product, ProductResponseDTO.class);
          dto.setRestaurantId(restId);
          return dto;
        })
        .toList();
  }

  @Transactional
  public List<ProductResponseDTO> getAllProducts() {
    return productRepository.findAll()
        .stream()
        .map(product -> {
          Long restId = product.getRestaurant().getId();
          ProductResponseDTO dto = mapper.map(product, ProductResponseDTO.class);
          dto.setRestaurantId(restId);
          return dto;
        })
        .toList();
  }

  @Transactional
  public ProductResponseDTO getProductById(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));
    Long restaurantId = product.getRestaurant().getId();
    ProductResponseDTO dto = mapper.map(product, ProductResponseDTO.class);
    dto.setRestaurantId(restaurantId);
    return dto;
  }

  @Transactional
  public ProductResponseDTO toggleProductAvailable(Long id) {
    Product product = productRepository.findById(id)
        .orElseThrow(() -> new EntityNotFoundException("Produto não encontrado"));

    product.setAvailable(!product.getAvailable());
    Product updatedProduct = productRepository.save(product);

    Long restId = updatedProduct.getRestaurant().getId();
    ProductResponseDTO dto = mapper.map(updatedProduct, ProductResponseDTO.class);
    dto.setRestaurantId(restId);
    return dto;
  }
}