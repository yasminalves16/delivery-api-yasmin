package com.deliverytech.delivery_api.dto;

import java.util.List;

import com.deliverytech.delivery_api.model.OrderItem;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderDTO {
  @NotBlank
  private String addressDelivery;

  @NotNull
  private Long customerId;

  @NotNull
  private Long restaurantId;

  @Valid
  @NotNull
  private List<OrderItem> items;
}
