package com.deliverytech.delivery_api.config;

import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.servers.Server;

@Configuration
public class OpenApiConfig {

  @Bean
  public OpenAPI customOpenAPI() {
    return new OpenAPI()
        .info(new Info()
            .title("Delivery API")
            .version("1.0")
            .contact(new Contact().name("Yasmin").email("yasmin.alves16@outlook.com"))
            .description("API para gerenciamento de entregas"))
        .servers(List.of(new Server()
            .url("http://localhost:8080")
            .description("Servidor local")));
  }

}
