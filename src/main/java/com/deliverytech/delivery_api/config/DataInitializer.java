package com.deliverytech.delivery_api.config;

import java.math.BigDecimal;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.RestaurantRepository;
import com.deliverytech.delivery_api.repository.UserRepository;

@Configuration
@Profile("!test")
public class DataInitializer implements CommandLineRunner {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final RestaurantRepository restaurantRepository;

  public DataInitializer(UserRepository userRepository, PasswordEncoder passwordEncoder,
      RestaurantRepository restaurantRepository) {
    this.userRepository = userRepository;
    this.passwordEncoder = passwordEncoder;
    this.restaurantRepository = restaurantRepository;
  }

  @Override
  public void run(String... args) {
    // if (!userRepository.existsByEmail("customer@gmail.com")) {
    // User customer = new User();
    // customer.setName("Customer Name");
    // customer.setEmail("customer@gmail.com");
    // customer.setPassword(passwordEncoder.encode("12345"));
    // customer.setRole(Role.CUSTOMER);
    // customer.setActive(true);
    // userRepository.save(customer);
    // }

    // if (!userRepository.existsByEmail("restaurant@gmail.com")) {
    // User restaurant = new User();
    // restaurant.setName("Restaurant Name");
    // restaurant.setEmail("restaurant@gmail.com");
    // restaurant.setPassword(passwordEncoder.encode("12345"));
    // restaurant.setRole(Role.RESTAURANT);
    // restaurant.setActive(true);
    // userRepository.save(restaurant);
    // }

    // if (!userRepository.existsByEmail("admin@gmail.com")) {
    // User admin = new User();
    // admin.setName("Admin Name");
    // admin.setEmail("admin@gmail.com");
    // admin.setPassword(passwordEncoder.encode("testando"));
    // admin.setRole(Role.ADMIN);
    // admin.setActive(true);
    // userRepository.save(admin);
    // }

    if (!userRepository.existsByEmail("admin@gmail.com")) {
      User admin = new User();
      admin.setName("Admin Name");
      admin.setEmail("admin@gmail.com");
      admin.setPassword(passwordEncoder.encode("testando"));
      admin.setRole(Role.ADMIN);
      admin.setActive(true);
      userRepository.save(admin);
    }

    User userRestaurant1 = userRepository.findByEmail("restaurant@gmail.com").orElseGet(() -> {
      User restaurant = new User();
      restaurant.setName("Restaurante 1");
      restaurant.setEmail("restaurant@gmail.com");
      restaurant.setPassword(passwordEncoder.encode("12345"));
      restaurant.setRole(Role.RESTAURANT);
      restaurant.setActive(true);
      return userRepository.save(restaurant);
    });

    if (!restaurantRepository.existsByName("Pizzaria 1")) {
      Restaurant restaurant1 = new Restaurant();
      restaurant1.setName("Pizzaria 1");
      restaurant1.setCategory("Pizzaria");
      restaurant1.setAddress("Rua das Flores, 123");
      restaurant1.setPhone("1234-5678");
      restaurant1.setDeliveryFee(BigDecimal.valueOf(5.00));
      restaurant1.setActive(true);
      restaurant1.setUser(userRestaurant1);
      restaurantRepository.save(restaurant1);
    }

    User userRestaurant2 = userRepository.findByEmail("restaurant2@gmail.com").orElseGet(() -> {
      User restaurant = new User();
      restaurant.setName("Restaurante 2");
      restaurant.setEmail("restaurant2@gmail.com");
      restaurant.setPassword(passwordEncoder.encode("12345"));
      restaurant.setRole(Role.RESTAURANT);
      restaurant.setActive(true);
      return userRepository.save(restaurant);
    });

    if (!restaurantRepository.existsByName("Pizzaria 2")) {
      Restaurant restaurant2 = new Restaurant();
      restaurant2.setName("Pizzaria 2");
      restaurant2.setCategory("Pizzaria");
      restaurant2.setAddress("Av. Central, 456");
      restaurant2.setPhone("9876-5432");
      restaurant2.setDeliveryFee(BigDecimal.valueOf(6.50));
      restaurant2.setActive(true);
      restaurant2.setUser(userRestaurant2);
      restaurantRepository.save(restaurant2);
    }

    System.out.println("Dados iniciais criados com sucesso!");
  }

}
