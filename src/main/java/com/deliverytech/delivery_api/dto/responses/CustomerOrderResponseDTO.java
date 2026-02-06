package com.deliverytech.delivery_api.dto.responses;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import com.deliverytech.delivery_api.enums.CustomerOrderStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerOrderResponseDTO {
  private Long id;

  private LocalDateTime dateOrder;

  private BigDecimal totalValue;

  private CustomerOrderStatus status;

  private String addressDelivery;

  private BigDecimal deliveryFee;

  private List<OrderItemResponseDTO> items;
  
  private String customerName;

  private String restaurantName;

}
