package com.deliverytech.delivery_api.dto.responses;

import com.deliverytech.delivery_api.enums.Role;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserMeResponseDTO {
  private Long id;
  private String name;
  private String email;
  private Role role;
  private Long restaurantId;
}
