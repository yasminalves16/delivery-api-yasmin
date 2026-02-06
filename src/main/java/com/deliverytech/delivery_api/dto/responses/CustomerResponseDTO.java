package com.deliverytech.delivery_api.dto.responses;

import java.time.LocalDateTime;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerResponseDTO {
  private Long id;
  
  private String name;

  private String email;

  private String phone;

  private String address;

  private Boolean active;

  private LocalDateTime createdAt;
}
