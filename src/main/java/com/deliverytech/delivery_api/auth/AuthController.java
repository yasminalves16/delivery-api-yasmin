package com.deliverytech.delivery_api.auth;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.dto.requests.AdminRegisterDTO;
import com.deliverytech.delivery_api.dto.requests.RegisterDTO;
import com.deliverytech.delivery_api.dto.responses.UserMeResponseDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.CustomerRepository;
import com.deliverytech.delivery_api.repository.UserRepository;
import com.deliverytech.delivery_api.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository repository;
  private final CustomerRepository customerRepository;

  private final PasswordEncoder encoder;

  private final JwtService jwtService;

  public AuthController(PasswordEncoder encoder, UserRepository repository,
      CustomerRepository customerRepository, JwtService jwtService) {
    this.encoder = encoder;
    this.repository = repository;
    this.customerRepository = customerRepository;
    this.jwtService = jwtService;
  }

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody RegisterDTO user) {
    if (repository.existsByEmail(user.getEmail()) || customerRepository.existsByEmail(user.getEmail())) {
      return ResponseEntity.badRequest().body("Email já registrado para outro usuário");
    }

    if (user.getRole() == Role.ADMIN) {
      return ResponseEntity.badRequest().body("Não é permitido registrar usuários com função ADMIN");
    }

    User newUser = new User();
    newUser.setName(user.getName());
    newUser.setEmail(user.getEmail());
    newUser.setPassword(encoder.encode(user.getPassword()));
    newUser.setRole(user.getRole());
    newUser.setActive(true);

    User savedUser = repository.save(newUser);

    return ResponseEntity.ok(savedUser);
  }

  @PostMapping("/login")
  public ResponseEntity<?> login(@RequestBody AuthRequest user) {
    User foundUser = repository.findByEmail(user.getEmail()).orElse(null);

    if (foundUser == null || !encoder.matches(user.getPassword(), foundUser.getPassword())) {
      return ResponseEntity.status(401).body("Credenciais inválidas");
    }

    String token = jwtService.generateToken(foundUser.getEmail());

    return ResponseEntity.ok(new AuthResponse(token));
  }

  @GetMapping("/me")
  @PreAuthorize("isAuthenticated()")
  public ResponseEntity<UserMeResponseDTO> getMe(Authentication authentication) {
    User user = (User) authentication.getPrincipal();

    UserMeResponseDTO response = new UserMeResponseDTO();
    response.setId(user.getId());
    response.setName(user.getName());
    response.setEmail(user.getEmail());
    response.setRole(user.getRole());
    if (user.getRestaurant() != null) {
      response.setRestaurantId(user.getRestaurant().getId());
    } else {
      response.setRestaurantId(user.getRestaurantId());
    }

    return ResponseEntity.ok(response);
  }

  @PostMapping("/admin/register")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> registerAdmin(@RequestBody AdminRegisterDTO adminUser) {
    if (repository.existsByEmail(adminUser.getEmail()) || customerRepository.existsByEmail(adminUser.getEmail())) {
      return ResponseEntity.badRequest().body("Email já registrado para outro usuário");
    }

    User newAdmin = new User();
    newAdmin.setName(adminUser.getName());
    newAdmin.setEmail(adminUser.getEmail());
    newAdmin.setPassword(encoder.encode(adminUser.getPassword()));
    newAdmin.setRole(Role.ADMIN);
    newAdmin.setActive(true);

    User savedAdmin = repository.save(newAdmin);

    UserMeResponseDTO response = new UserMeResponseDTO();
    response.setId(savedAdmin.getId());
    response.setName(savedAdmin.getName());
    response.setEmail(savedAdmin.getEmail());
    response.setRole(savedAdmin.getRole());

    return ResponseEntity.status(201).body(response);
  }

  @DeleteMapping("/admin/{adminId}")
  @PreAuthorize("hasRole('ADMIN')")
  public ResponseEntity<?> deleteAdmin(@PathVariable Long adminId, Authentication authentication) {
    User loggedAdmin = (User) authentication.getPrincipal();

    if (!Long.valueOf(1L).equals(loggedAdmin.getId())) {
      return ResponseEntity.status(403).body("Somente o admin id 1 pode excluir outros admins.");
    }

    if (Long.valueOf(1L).equals(adminId) || adminId.equals(loggedAdmin.getId())) {
      return ResponseEntity.badRequest().body("Admins não podem se excluir.");
    }

    User targetAdmin = repository.findById(adminId).orElse(null);
    if (targetAdmin == null) {
      return ResponseEntity.status(404).body("Admin não encontrado.");
    }

    if (targetAdmin.getRole() != Role.ADMIN) {
      return ResponseEntity.badRequest().body("O usuário informado não possui perfil ADMIN.");
    }

    repository.delete(targetAdmin);
    return ResponseEntity.noContent().build();
  }

  @RestController
  @RequestMapping("/api/customer-orders")
  public class CustomerOrdersController {
    @GetMapping("/my-orders")
    public ResponseEntity<?> getMyOrders() {
      return ResponseEntity.ok("Aqui estarão os pedidos do cliente");
    }
  }
}