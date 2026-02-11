package com.deliverytech.delivery_api.controller;

import java.net.URI;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.CacheControl;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.deliverytech.delivery_api.dto.requests.CustomerDTO;
import com.deliverytech.delivery_api.dto.responses.ApiResponse;
import com.deliverytech.delivery_api.dto.responses.CustomerResponseDTO;
import com.deliverytech.delivery_api.dto.responses.PagedResponse;
import com.deliverytech.delivery_api.service.CustomerService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

@RestController
@RequestMapping(value = "/customers", produces = "application/json")
@CrossOrigin(origins = "*", allowedHeaders = "*")
@Tag(name = "Customer", description = "Endpoints para gerenciamento de clientes")
public class CustomerController {
  @Autowired
  private final CustomerService customerService;

  public CustomerController(CustomerService customerService) {
    this.customerService = customerService;
  }

  @Operation(summary = "Registrar um novo cliente", description = "Cria um novo cliente com as informações fornecidas")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "201", description = "Cliente criado com sucesso"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "Requisição inválida")
  })
  @PostMapping
  public ResponseEntity<ApiResponse<CustomerResponseDTO>> registerCustomer(@Valid @RequestBody CustomerDTO customer) {
    CustomerResponseDTO registeredCustomer = customerService.registerCustomer(customer);
    URI location = ServletUriComponentsBuilder.fromCurrentRequest()
        .path("/{id}")
        .buildAndExpand(registeredCustomer.getId())
        .toUri();
    return ResponseEntity.created(location).header("Content-Type", "application/json")
        .body(new ApiResponse<>(registeredCustomer));
  }

  @Operation(summary = "Obter clientes ativos", description = "Retorna uma lista de clientes ativos")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de clientes ativos retornada com sucesso"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Nenhum cliente ativo encontrado")
  })
  @GetMapping
  public ResponseEntity<PagedResponse<CustomerResponseDTO>> getActiveCustomers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    var pageResult = customerService.getActiveCustomers(pageable);
    var response = new PagedResponse<>(pageResult);
    return ResponseEntity.ok().header("Content-Type", "application/json").cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(response);
  }

  @Operation(summary = "Obter todos os clientes", description = "Retorna uma lista de todos os clientes, incluindo inativos")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de clientes retornada com sucesso"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado")
  })
  @GetMapping("/all")
  public ResponseEntity<PagedResponse<CustomerResponseDTO>> getAllCustomers(
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    var pageResult = customerService.getAllCustomers(pageable);
    var response = new PagedResponse<>(pageResult);
    return ResponseEntity.ok().header("Content-Type", "application/json").cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(response);
  }

  @Operation(summary = "Buscar clientes por nome", description = "Retorna uma lista de clientes que correspondem ao nome fornecido")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Lista de clientes que correspondem ao nome retornada com sucesso"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Nenhum cliente encontrado com o nome fornecido")
  })
  @GetMapping("/search")
  public ResponseEntity<PagedResponse<CustomerResponseDTO>> searchCustomersByName(
      @RequestParam String name,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int size) {
    Pageable pageable = PageRequest.of(page, size);
    var pageResult = customerService.searchCustomersByName(name, pageable);
    var response = new PagedResponse<>(pageResult);
    return ResponseEntity.ok().header("Content-Type", "application/json").cacheControl(CacheControl.maxAge(60, TimeUnit.SECONDS)).body(response);
  }

  @Operation(summary = "Buscar cliente por ID", description = "Retorna um cliente que corresponde ao ID fornecido")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Cliente encontrado com sucesso"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente não encontrado com esse ID")
  })
  @GetMapping("/{id}")
  public ResponseEntity<ApiResponse<CustomerResponseDTO>> searchCustomerById(@PathVariable Long id) {
    return ResponseEntity.ok().header("Content-Type", "application/json").body(new ApiResponse<>(customerService.searchCustomerById(id)));
  }

  // @PutMapping("/{id}")
  // public CustomerResponseDTO updateCustomer(@PathVariable Long id, @RequestBody
  // Customer updatedCustomer) {
  // return customerService.updateCustomer(id, updatedCustomer);
  // }

  @Operation(summary = "Ativar/Inativar cliente", description = "Altera o status de ativo do cliente, permitindo ativar ou inativar um cliente")
  @ApiResponses(value = {
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "200", description = "Status do cliente alterado com sucesso"),
      @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "404", description = "Cliente não encontrado com esse ID")
  })
  @PatchMapping("/{id}/toggle")
  public ResponseEntity<ApiResponse<CustomerResponseDTO>> toggleCustomerActive(@PathVariable Long id) {
    return ResponseEntity.ok().header("Content-Type", "application/json").body(new ApiResponse<>(customerService.toggleCustomerActive(id)));
  }

}
