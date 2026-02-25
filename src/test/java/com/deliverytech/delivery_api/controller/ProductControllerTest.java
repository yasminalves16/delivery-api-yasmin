package com.deliverytech.delivery_api.controller;

import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.requests.ProductDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.RestaurantRepository;
import com.deliverytech.delivery_api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
@WithMockUser(username = "rest.product@test.com", roles = { "RESTAURANT" })
public class ProductControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private RestaurantRepository restaurantRepository;

  @Autowired
  private UserRepository userRepository;

  private Long validRestaurantId;

  @BeforeEach
  void setUp() {
    User restaurantUser = new User();
    restaurantUser.setName("Restaurant Product User");
    restaurantUser.setEmail("rest.product@test.com");
    restaurantUser.setPassword("12345");
    restaurantUser.setRole(Role.RESTAURANT);
    restaurantUser.setActive(true);
    restaurantUser = userRepository.save(restaurantUser);

    Restaurant restaurant = new Restaurant();
    restaurant.setName("Restaurant Product Test " + System.currentTimeMillis());
    restaurant.setCategory("Variada");
    restaurant.setAddress("Rua dos Produtos, 10");
    restaurant.setPhone("11988887777");
    restaurant.setDeliveryFee(new BigDecimal("7.50"));
    restaurant.setActive(true);
    restaurant.setUser(restaurantUser);
    restaurant = restaurantRepository.save(restaurant);

    restaurantUser.setRestaurant(restaurant);
    restaurantUser.setRestaurantId(restaurant.getId());
    userRepository.save(restaurantUser);

    validRestaurantId = restaurant.getId();
  }

  private ProductDTO createProductDTO() {
    ProductDTO dto = new ProductDTO();
    dto.setName("X-Burger Especial");
    dto.setDescription("Pão, carne de 180g e muito queijo.");
    dto.setCategory("Lanches");
    dto.setImageUrl("https://img.com/xburger-especial.png");
    dto.setPrice(new BigDecimal("35.90"));
    return dto;
  }

  @Test
  void testRegisterProductSuccess() throws Exception {
    ProductDTO dto = createProductDTO();

    mockMvc.perform(post("/products/restaurant/{restaurantId}", validRestaurantId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.name", is("X-Burger Especial")))
        .andExpect(jsonPath("$.price", is(35.90)));
  }

  @Test
  void testReturnNotFoundWhenRestaurantDoesNotExist() throws Exception {
    ProductDTO dto = createProductDTO();

    mockMvc.perform(post("/products/restaurant/{restaurantId}", 999999L)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
       .andExpect(status().isConflict());
  }

  @Test
  void testReturnBadRequestForNegativePrice() throws Exception {
    ProductDTO dto = createProductDTO();
    dto.setPrice(new BigDecimal("-5.00"));

    mockMvc.perform(post("/products/restaurant/{restaurantId}", validRestaurantId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(dto)))
        .andExpect(status().isBadRequest());
  }

  @Test
  void testToggleProductAvailability() throws Exception {
    String response = mockMvc.perform(post("/products/restaurant/{restaurantId}", validRestaurantId)
        .contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(createProductDTO())))
        .andExpect(status().isCreated())
        .andReturn()
        .getResponse()
        .getContentAsString();

    Integer idInt = JsonPath.read(response, "$.id");
    Long productId = idInt.longValue();

    mockMvc.perform(patch("/products/{id}/toggle", productId))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.available", is(false)));
  }
}
