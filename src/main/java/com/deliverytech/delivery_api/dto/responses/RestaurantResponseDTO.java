package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantResponseDTO {
  private String name;

  private String category;

  private String address;

  private String phone;

  private BigDecimal rating;

  private BigDecimal deliveryFee;
  
  private Boolean active;
}
