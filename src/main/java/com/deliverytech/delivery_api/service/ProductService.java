package com.deliverytech.delivery_api.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;

@Service
public class ProductService {
  private final ProductRepository productRepository;
  private final RestaurantRepository restaurantRepository;

  public ProductService(ProductRepository productRepository, RestaurantRepository restaurantRepository) {
    this.productRepository = productRepository;
    this.restaurantRepository = restaurantRepository;
  }

  public Product registerProduct(Long restaurantId, Product product) {
    if (product.getPrice() == null || product.getPrice().compareTo(BigDecimal.ZERO) <= 0) {
      throw new IllegalArgumentException("O preço do produto deve ser maior que zero");
    }

    Restaurant restaurant = restaurantRepository.findById(restaurantId)
        .orElseThrow(() -> new IllegalArgumentException("Restaurante não encontrado"));
    product.setAvailable(true);
    product.setRestaurant(restaurant);
    return productRepository.save(product);
  }

  public List<Product> getProductsByRestaurantId(Long restaurantId) {
    return productRepository.findByRestaurantId(restaurantId);
  }

  public List<Product> getAllAvailableProducts() {
    return productRepository.findByAvailableTrue();
  }

  public Product getProductById(Long id) {
    return productRepository.findById(id)
        .orElseThrow(() -> new IllegalArgumentException("Produto não encontrado"));
  }
}