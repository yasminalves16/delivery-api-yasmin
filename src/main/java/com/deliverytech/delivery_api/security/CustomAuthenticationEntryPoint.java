package com.deliverytech.delivery_api.security;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {

  private final ObjectMapper objectMapper;

  public CustomAuthenticationEntryPoint(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void commence(HttpServletRequest request,
      HttpServletResponse response,
      AuthenticationException authException) throws IOException {

    String path = request.getRequestURI();
    String method = request.getMethod();

    String message = "Acesso inválido: é necessário estar autenticado e enviar um Bearer token válido.";

    if (path.equals("/restaurants") && HttpMethod.POST.matches(method)) {
      message = "Acesso inválido: é preciso estar logado com uma conta RESTAURANT ou ADMIN para criar um restaurante.";
    }

    ErrorResponse body = new ErrorResponse(
        HttpServletResponse.SC_UNAUTHORIZED,
        message,
        System.currentTimeMillis(),
        null);

    response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), body);
  }
}
