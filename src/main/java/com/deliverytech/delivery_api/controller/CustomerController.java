package com.deliverytech.delivery_api.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.CustomerDTO;
import com.deliverytech.delivery_api.dto.responses.CustomerResponseDTO;
import com.deliverytech.delivery_api.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/customers")
@Tag(name = "Customer", description = "Endpoints para gerenciamento de clientes")
public class CustomerController {
  @Autowired
  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @Operation(summary = "Registrar um novo cliente", description = "Cria um novo cliente com as informações fornecidas")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
      @ApiResponse(responseCode = "400", description = "Requisição inválida")
  })
  @PostMapping
  public ResponseEntity<CustomerResponseDTO> registerCustomer(@Valid @RequestBody CustomerDTO customer) {
    return ResponseEntity.status(HttpStatus.CREATED).body(customerService.registerCustomer(customer));
  }

  @Operation(summary = "Obter clientes ativos", description = "Retorna uma lista de clientes ativos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Nenhum cliente ativo encontrado")
  })
  @GetMapping
  public List<CustomerResponseDTO> getActiveCustomers() {
    return customerService.getActiveCustomers();
  }

  @Operation(summary = "Obter todos os clientes", description = "Retorna uma lista de todos os clientes, incluindo inativos")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado")
  })
  @GetMapping("/all")
  public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
    List<CustomerResponseDTO> allCustomers = customerService.getAllCustomers();
    return ResponseEntity.ok(allCustomers);
  }

  @Operation(summary = "Buscar clientes por nome", description = "Retorna uma lista de clientes que correspondem ao nome fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Lista de clientes que correspondem ao nome retornada com sucesso"),
      @ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado com o nome fornecido")
  })
  @GetMapping("/search")
  public List<CustomerResponseDTO> searchCustomersByName(@RequestParam String name) {
    return customerService.searchCustomersByName(name);
  }

  @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente que corresponde ao ID fornecido")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado com esse ID")
  })
  @GetMapping("/{id}")
  public CustomerResponseDTO searchCustomerById(@PathVariable Long id) {
    return customerService.searchCustomerById(id);
  }

  // @PutMapping("/{id}")
  // public CustomerResponseDTO updateCustomer(@PathVariable Long id, @RequestBody
  // Customer updatedCustomer) {
  // return customerService.updateCustomer(id, updatedCustomer);
  // }
  
  @Operation(summary = "Ativar/Inativar cliente", description = "Altera o status de ativo do cliente, permitindo ativar ou inativar um cliente")
  @ApiResponses(value = {
      @ApiResponse(responseCode = "200", description = "Status do cliente alterado com sucesso"),
      @ApiResponse(responseCode = "404", description = "Cliente não encontrado com esse ID")
  })
  @PatchMapping("/{id}/toggle")
  public CustomerResponseDTO toggleCustomerActive(@PathVariable Long id) {
    return customerService.toggleCustomerActive(id);
  }
}
