package com.deliverytech.delivery_api.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ProductDTO {
  
  @NotBlank(message = "Name é obrigatório")
  private String name;

  @NotBlank(message = "Description é obrigatória")
  @Size(min = 5, message = "Description deve ter ao menos 5 caracteres")
  private String description;

  @NotBlank(message = "Category é obrigatória")
  private String category;

  @NotBlank(message = "Image URL é obrigatória")
  private String imageUrl;

  @NotNull(message = "Price é obrigatório")
  @Positive(message = "Price deve ser positivo")
  @DecimalMin(value = "0.01", message = "Price deve ser maior que zero")
  private BigDecimal price;

}