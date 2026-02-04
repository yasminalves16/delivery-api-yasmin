package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductResponseDTO {
  private Long id;
  
  private String name;

  private String description;

  private String category;

  private String imageUrl;

  private BigDecimal price;

  private Boolean available;

  private Long restaurantId;
}