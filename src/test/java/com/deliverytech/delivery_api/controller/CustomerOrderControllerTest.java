package com.deliverytech.delivery_api.controller;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.requests.CustomerOrderDTO;
import com.deliverytech.delivery_api.dto.requests.OrderItemDTO;
import com.deliverytech.delivery_api.enums.Role;
import com.deliverytech.delivery_api.model.Customer;
import com.deliverytech.delivery_api.model.Product;
import com.deliverytech.delivery_api.model.Restaurant;
import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.CustomerRepository;
import com.deliverytech.delivery_api.repository.ProductRepository;
import com.deliverytech.delivery_api.repository.RestaurantRepository;
import com.deliverytech.delivery_api.repository.UserRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
public class CustomerOrderControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@Autowired
	private CustomerRepository customerRepository;

	@Autowired
	private RestaurantRepository restaurantRepository;

	@Autowired
	private ProductRepository productRepository;

	@Autowired
	private UserRepository userRepository;

	private Long customerId;
	private Long restaurantId;
	private Long productId;

	@BeforeEach
	void setUp() {
		Customer customer = new Customer();
		customer.setName("Joao Silva");
		customer.setEmail("customer." + System.currentTimeMillis() + "@email.com");
		customer.setPhone("11999990000");
		customer.setAddress("Rua Teste, 123");
		customer.setActive(true);
		customer = customerRepository.save(customer);
		customerId = customer.getId();

		Restaurant restaurant = new Restaurant();
		restaurant.setName("Burger House " + System.currentTimeMillis());
		restaurant.setCategory("Lanches");
		restaurant.setAddress("Rua dos Restaurantes, 10");
		restaurant.setPhone("11988887777");
		restaurant.setDeliveryFee(BigDecimal.valueOf(5.0));
		restaurant.setActive(true);

		User restaurantUser = new User();
		restaurantUser.setName("Restaurant Test User");
		restaurantUser.setEmail("rest.order." + System.currentTimeMillis() + "@email.com");
		restaurantUser.setPassword("12345");
		restaurantUser.setRole(Role.RESTAURANT);
		restaurantUser.setActive(true);
		restaurantUser = userRepository.save(restaurantUser);

		restaurant.setUser(restaurantUser);
		restaurant = restaurantRepository.save(restaurant);
		restaurantUser.setRestaurant(restaurant);
		restaurantUser.setRestaurantId(restaurant.getId());
		userRepository.save(restaurantUser);
		restaurantId = restaurant.getId();

		Product product = new Product();
		product.setName("X-Burger");
		product.setDescription("Pão, carne e queijo");
		product.setCategory("Lanches");
		product.setImageUrl("https://img.com/xburger.png");
		product.setPrice(BigDecimal.valueOf(30.0));
		product.setAvailable(true);
		product.setRestaurant(restaurant);
		product = productRepository.save(product);
		productId = product.getId();
	}

	private CustomerOrderDTO createOrderDTO() {
		CustomerOrderDTO dto = new CustomerOrderDTO();
		dto.setCustomerId(customerId);
		dto.setRestaurantId(restaurantId);
		dto.setAddressDelivery("Rua Entrega, 100");

		OrderItemDTO item = new OrderItemDTO();
		item.setProductId(productId);
		item.setQuantity(2);

		dto.setItems(List.of(item));
		return dto;
	}

	@Test
	void testCreateOrderSuccess() throws Exception {
		mockMvc.perform(post("/customer-orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createOrderDTO())))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.status", is("PENDENTE")))
				.andExpect(jsonPath("$.customerName", is("Joao Silva")));
	}

	@Test
	void testReturnConflictWhenAdvancingPendingOrder() throws Exception {
		String response = mockMvc.perform(post("/customer-orders")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createOrderDTO())))
				.andExpect(status().isCreated())
				.andReturn()
				.getResponse()
				.getContentAsString();

		Integer idInt = JsonPath.read(response, "$.id");
		Long orderId = idInt.longValue();

		mockMvc.perform(put("/customer-orders/{orderId}/status", orderId))
				.andExpect(status().isConflict())
				.andExpect(jsonPath("$.message", containsString("Status atual do pedido")));
	}

	@Test
	void testReturnEmptyListForNonExistentCustomerOrders() throws Exception {
		mockMvc.perform(get("/customer-orders/customer/{customerId}", 999999L))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.content", hasSize(0)));
	}

	@Test
	void testValidatePaginationMetadata() throws Exception {
		mockMvc.perform(get("/customer-orders/customer/{customerId}", customerId)
				.param("page", "0")
				.param("size", "10"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.page", is(0)))
				.andExpect(jsonPath("$.totalElements").exists());
	}
}
