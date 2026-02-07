package com.deliverytech.delivery_api.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
public class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testRegisterCustomerSuccess() throws Exception {
    String customerJson = """
        {
          "name": "Yasmin",
          "email": "yasmin@example.com",
          "phone": "11987654321",
          "address": "Rua das Flores, 123"
        }
    """;
    mockMvc.perform(post("/customers")
            .contentType(MediaType.APPLICATION_JSON)
            .content(customerJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id").exists())
        .andExpect(jsonPath("$.name").value("Yasmin"))
        .andExpect(jsonPath("$.email").value("yasmin@example.com"))
        .andExpect(jsonPath("$.phone").value("11987654321"))
        .andExpect(jsonPath("$.address").value("Rua das Flores, 123"));
  }
}
