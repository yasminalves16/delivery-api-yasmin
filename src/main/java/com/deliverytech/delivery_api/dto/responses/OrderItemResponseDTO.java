package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemResponseDTO {
  private Long id;

  private Long productId;

  private String productName;

  private Integer quantity;

  private BigDecimal unitPrice;

  private BigDecimal subtotal;

}
