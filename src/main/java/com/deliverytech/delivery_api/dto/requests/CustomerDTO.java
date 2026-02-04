package com.deliverytech.delivery_api.dto.requests;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CustomerDTO {
  @NotBlank(message = "Nome é obrigatório")
  private String name;

  @Email(message = "Email inválido")
  @NotBlank(message = "Email é obrigatório")
  private String email;

  @NotBlank(message = "Telefone é obrigatório")
  @Pattern(regexp = "\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}", message = "Telefone inválido. Formato esperado: (XX) XXXXX-XXXX")
  private String phone;

  @Size(min = 5, message = "Endereço deve ter ao menos 5 caracteres")
  private String address;
}
