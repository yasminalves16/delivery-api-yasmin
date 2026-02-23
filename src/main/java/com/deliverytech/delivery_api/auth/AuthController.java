package com.deliverytech.delivery_api.auth;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.UserRepository;
import com.deliverytech.delivery_api.security.JwtService;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

  private final UserRepository repository;

  private final PasswordEncoder encoder;

  private final JwtService jwtService;

  public AuthController(PasswordEncoder encoder, UserRepository repository,
      JwtService jwtService) {
    this.encoder = encoder;
    this.repository = repository;
    this.jwtService = jwtService;
  }

  @PostMapping("/login")
  public String login(@RequestBody User user) {
    User foundUser = repository.findByEmail(user.getEmail()).orElseThrow();

    if (!encoder.matches(user.getPassword(), foundUser.getPassword())) {
      throw new RuntimeException("Credenciais inválidas");
    }
    return jwtService.generateToken(foundUser.getEmail());
  }
}