package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RestaurantDTO {
  @NotNull
  @NotBlank
  private String name;

  @NotBlank
  private String category;

  @Size(max = 500)
  private String address;

  @NotBlank
  private String phone;

  @NotNull
  private BigDecimal deliveryFee;

}
