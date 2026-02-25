package com.deliverytech.delivery_api.security;

import java.io.IOException;

import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.exceptions.ErrorResponse;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler {

  private final ObjectMapper objectMapper;

  public CustomAccessDeniedHandler(ObjectMapper objectMapper) {
    this.objectMapper = objectMapper;
  }

  @Override
  public void handle(HttpServletRequest request,
      HttpServletResponse response,
      AccessDeniedException accessDeniedException) throws IOException {

    String path = request.getRequestURI();
    String method = request.getMethod();

    String message = "Acesso negado: você não possui permissão para este recurso.";

    if (path.equals("/restaurants") && HttpMethod.POST.matches(method)) {
      message = "Não é possível criar restaurante com conta CLIENTE: use uma conta RESTAURANT ou ADMIN.";
    } else if (path.matches("^/restaurants/\\d+/toggle$") && HttpMethod.PATCH.matches(method)) {
      message = "Não autorizado: somente ADMIN ou o dono do restaurante pode alterar o status do restaurante.";
    }

    if (path.startsWith("/products/") &&
        (HttpMethod.POST.matches(method)
            || HttpMethod.PATCH.matches(method)
            || HttpMethod.PUT.matches(method)
            || HttpMethod.DELETE.matches(method))) {
      message = "Não autorizado: somente admins ou restaurantes podem alterar produtos (cadastrar, editar, deletar).";
    }

    ErrorResponse body = new ErrorResponse(
        HttpServletResponse.SC_FORBIDDEN,
        message,
        System.currentTimeMillis(),
        null);

    response.setStatus(HttpServletResponse.SC_FORBIDDEN);
    response.setContentType(MediaType.APPLICATION_JSON_VALUE);
    response.setCharacterEncoding("UTF-8");
    objectMapper.writeValue(response.getWriter(), body);
  }
}