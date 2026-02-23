package com.deliverytech.delivery_api.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.UserRepository;

@Configuration
@Profile("!test")
public class DataInitializer implements CommandLineRunner {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;

  public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
  }

  @Override
  public void run(String... args) {
    if (!userRepository.existsByEmail("customer@gmail.com")) {
      User customer = new User();
      customer.setName("Customer Name");
      customer.setEmail("customer@gmail.com");
      customer.setPassword(passwordEncoder.encode("12345"));
      customer.setRole(Role.CUSTOMER);
      customer.setActive(true);
      userRepository.save(customer);
    }

    if (!userRepository.existsByEmail("restaurant@gmail.com")) {
      User restaurant = new User();
      restaurant.setName("Restaurant Name");
      restaurant.setEmail("restaurant@gmail.com");
      restaurant.setPassword(passwordEncoder.encode("12345"));
      restaurant.setRole(Role.RESTAURANT);
      restaurant.setActive(true);
      userRepository.save(restaurant);
    }

    if (!userRepository.existsByEmail("admin@gmail.com")) {
      User admin = new User();
      admin.setName("Admin Name");
      admin.setEmail("admin@gmail.com");
      admin.setPassword(passwordEncoder.encode("testando"));
      admin.setRole(Role.ADMIN);
      admin.setActive(true);
      userRepository.save(admin);
    }
    System.out.println("Dados iniciais criados com sucesso!");
  }

}
