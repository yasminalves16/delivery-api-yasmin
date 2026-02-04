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
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.ProductDTO;
import com.deliverytech.delivery_api.dto.responses.ProductResponseDTO;
import com.deliverytech.delivery_api.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {
  private final ProductService productService;

  public ProductController(ProductService productService) {
    this.productService = productService;
  }

  @PostMapping("/restaurant/{restaurantId}")
  public ResponseEntity<ProductResponseDTO> registerProduct(@PathVariable Long restaurantId,
      @RequestBody @Valid ProductDTO product) {
    ProductResponseDTO response = productService.registerProduct(restaurantId, product);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping("/restaurant/{restaurantId}")
  public ResponseEntity<List<ProductResponseDTO>> getProductsByRestaurantId(@PathVariable Long restaurantId) {
    return ResponseEntity.ok(productService.getProductsByRestaurantId(restaurantId));
  }

  @GetMapping
  public ResponseEntity<List<ProductResponseDTO>> getAllAvailableProducts() {
    return ResponseEntity.ok(productService.getAllAvailableProducts());
  }

  @GetMapping("/all")
  public ResponseEntity<List<ProductResponseDTO>> getAllProducts() {
    return ResponseEntity.ok(productService.getAllProducts());
  }

  @GetMapping("/{id}")
  public ResponseEntity<ProductResponseDTO> getById(@PathVariable Long id) {
    return ResponseEntity.ok(productService.getProductById(id));
  }

  @PatchMapping("/{id}/toggle")
  public ResponseEntity<ProductResponseDTO> toggleProductAvailable(@PathVariable Long id) {
    return ResponseEntity.ok(productService.toggleProductAvailable(id));
  }
}
