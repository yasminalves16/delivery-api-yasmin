package com.deliverytech.delivery_api.dto.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "DTO para criação e atualização de clientes")
public class CustomerDTO {
  @Schema(description = "Nome do cliente", example = "João Silva")
  @NotBlank(message = "Nome é obrigatório")
  private String name;

  @Schema(description = "Email do cliente", example = "joao.silva@example.com")
  @Email(message = "Email inválido")
  @NotBlank(message = "Email é obrigatório")
  private String email;

  @Schema(description = "Telefone do cliente", example = "(11) 98765-4321")
  @NotBlank(message = "Telefone é obrigatório")
  @Pattern(regexp = "\\(?\\d{2}\\)?[\\s-]?\\d{4,5}-?\\d{4}", message = "Telefone inválido. Formato esperado: (XX) XXXXX-XXXX")
  private String phone;

  @Schema(description = "Endereço do cliente", example = "Rua das Flores, 123")
  @Size(min = 5, message = "Endereço deve ter ao menos 5 caracteres")
  private String address;
}
