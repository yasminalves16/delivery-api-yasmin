package com.deliverytech.delivery_api.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import com.jayway.jsonpath.JsonPath;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class CustomerControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @Test
  void testRegisterCustomerSuccess() throws Exception {
    String email = "novo." + System.currentTimeMillis() + "@email.com";
    String customerJson = """
            {
              "name": "Yasmin",
              "email": "%s",
              "phone": "11987654321",
              "address": "Rua das Flores, 123"
            }
        """.formatted(email);
    mockMvc.perform(post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(customerJson))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.data.id").exists())
        .andExpect(jsonPath("$.data.name").value("Yasmin"))
        .andExpect(jsonPath("$.data.email").value(email))
        .andExpect(jsonPath("$.data.phone").value("11987654321"))
        .andExpect(jsonPath("$.data.address").value("Rua das Flores, 123"))
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void testRegisterCustomerInvalidData() throws Exception {
    String customerJson = """
            {
              "name": "",
              "email": "invalid-email",
              "phone": "123"
            }
        """;
    mockMvc.perform(post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(customerJson))
        .andDo(result -> {
          System.out.println(result.getResponse().getStatus());
          System.out.println(result.getResponse().getContentAsString());
        })
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.errors").exists())
        .andExpect(jsonPath("$.timestamp").exists());
  }

  @Test
  void testListActiveCustomers() throws Exception {
    mockMvc.perform(get("/customers"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.content").isArray());
  }

  @Test
  void testSearchCustomerById() throws Exception {
    String email = "customer." + System.currentTimeMillis() + "@email.com";
    String customerJson = """
            {
              "name": "Joao Silva",
              "email": "%s",
              "phone": "11987654321",
              "address": "Rua A, 5"
            }
        """.formatted(email);

    MvcResult result = mockMvc.perform(post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(customerJson))
        .andExpect(status().isCreated())
        .andReturn();

    String createdCustomer = result.getResponse().getContentAsString();
    Integer idInt = JsonPath.read(createdCustomer, "$.data.id");
    Long id = idInt.longValue();

    mockMvc.perform(get("/customers/{id}", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.name").value("Joao Silva"))
        .andExpect(jsonPath("$.data.email").value(email));
  }

  @Test
  void testSearchCustomerByIdNotFound() throws Exception {
    mockMvc.perform(get("/customers/{id}", 999999L))
        .andExpect(status().isNotFound());
  }

  @Test
  void testToggleCustomerActive() throws Exception {
    String email = "toggle." + System.currentTimeMillis() + "@email.com";
    String customerJson = """
            {
              "name": "Joao Silva",
              "email": "%s",
              "phone": "11987654321",
              "address": "Rua A, 5"
            }
        """.formatted(email);

    MvcResult result = mockMvc.perform(post("/customers")
        .contentType(MediaType.APPLICATION_JSON)
        .content(customerJson))
        .andExpect(status().isCreated())
        .andReturn();

    String createdCustomer = result.getResponse().getContentAsString();
    Integer idInt = JsonPath.read(createdCustomer, "$.data.id");
    Long id = idInt.longValue();

    mockMvc.perform(org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch("/customers/{id}/toggle", id))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.active").value(false));
  }


}
