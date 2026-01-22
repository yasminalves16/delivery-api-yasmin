package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.service.ProductService;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("/{restaurantId}")
  public ResponseEntity<Product> registerProduct(@PathVariable Long restaurantId, @RequestBody Product product) {
    return ResponseEntity.status(201).body(productService.registerProduct(restaurantId, product));
  }

  @GetMapping("/restaurant/{restaurantId}")
  public List<Product> getProductsByRestaurantId(@PathVariable Long restaurantId) {
    return productService.getProductsByRestaurantId(restaurantId);
  }

  @GetMapping
  public List<Product> getAllAvailableProducts() {
    return productService.getAllAvailableProducts();
  }

  @GetMapping("/{id}")
  public ResponseEntity<Product> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

}
