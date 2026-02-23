package com.deliverytech.delivery_api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter extends GenericFilter {

  private final JwtService jwtService;

  public JwtAuthenticationFilter(JwtService jwtService) {
    this.jwtService = jwtService;
  }

  @Override
  public void doFilter(ServletRequest request,
      ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;

    String authHeader = req.getHeader("Authorization");

    if (authHeader != null && authHeader.startsWith("Bearer ")) {

      String token = authHeader.substring(7);

      if (jwtService.isTokenValid(token)) {

        String email = jwtService.extractEmail(token);

        UsernamePasswordAuthenticationToken auth = new UsernamePasswordAuthenticationToken(
            email, null, null);

        auth.setDetails(
            new WebAuthenticationDetailsSource()
                .buildDetails(req));

        SecurityContextHolder.getContext()
            .setAuthentication(auth);
      }
    }

    chain.doFilter(request, response);
  }
}
