package com.deliverytech.delivery_api.dto.requests;

import com.deliverytech.delivery_api.enums.Role;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RegisterDTO {

  @NotBlank
  private String name;

  @NotBlank
  @Email
  private String email;

  @NotBlank
  @Size(min = 6)
  private String password;

  @NotNull
  private Role role;

}
