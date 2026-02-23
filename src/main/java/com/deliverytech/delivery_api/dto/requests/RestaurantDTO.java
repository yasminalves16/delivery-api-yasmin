package com.deliverytech.delivery_api.dto.requests;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
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
  @Pattern(
    regexp = "\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}",
    message = "Invalid phone. Expected format: (XX) XXXXX-XXXX or similar"
  )
  private String phone;

  @NotNull
  private BigDecimal deliveryFee;

}
