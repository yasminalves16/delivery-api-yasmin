package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import com.deliverytech.delivery_api.dto.requests.RestaurantDTO;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@Transactional
@ActiveProfiles("test")
public class RestaurantControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private RestaurantDTO createValidDTO() {
		RestaurantDTO dto = new RestaurantDTO();
		dto.setName("Pizzaria do Bairro");
		dto.setCategory("Pizzaria");
		dto.setAddress("Rua das Flores, 123");
		dto.setPhone("(11) 98765-4321");
		dto.setDeliveryFee(new BigDecimal("10.50"));
		return dto;
	}

	@Test
	void testRegisterRestaurantSuccess() throws Exception {
		RestaurantDTO dto = createValidDTO();

		mockMvc.perform(post("/restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name").value("Pizzaria do Bairro"));
	}

	@Test
	void testReturnBadRequestForInvalidPhone() throws Exception {
		RestaurantDTO dto = createValidDTO();
		dto.setPhone("123");

		mockMvc.perform(post("/restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isBadRequest());
	}

	@Test
	void testReturnNotFoundForNonExistentRestaurant() throws Exception {
		mockMvc.perform(get("/restaurants/{id}", 999999L))
				.andExpect(status().isNotFound());
	}

	@Test
	void testValidatePaginationAndMetadata() throws Exception {
		mockMvc.perform(post("/restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(createValidDTO())));

		mockMvc.perform(get("/restaurants")
				.param("page", "0")
				.param("size", "5"))
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.page").value(0))
				.andExpect(jsonPath("$.size").value(5))
				.andExpect(jsonPath("$.content").isArray());
	}

	@Test
	void testReturnConflictForDuplicateRestaurant() throws Exception {
		RestaurantDTO dto = createValidDTO();

		mockMvc.perform(post("/restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isCreated());

		mockMvc.perform(post("/restaurants")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(dto)))
				.andExpect(status().isConflict());
	}
}
