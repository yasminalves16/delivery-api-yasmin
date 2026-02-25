package com.deliverytech.delivery_api.security;

import java.io.IOException;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;

import com.deliverytech.delivery_api.model.User;
import com.deliverytech.delivery_api.repository.UserRepository;

import jakarta.servlet.FilterChain;
import jakarta.servlet.GenericFilter;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.HttpServletRequest;

@Component
public class JwtAuthenticationFilter extends GenericFilter {

  private final JwtService jwtService;
  private final UserRepository userRepository;

  public JwtAuthenticationFilter(JwtService jwtService, UserRepository userRepository) {
    this.jwtService = jwtService;
    this.userRepository = userRepository;
  }

  @Override
  public void doFilter(ServletRequest request,
      ServletResponse response,
      FilterChain chain)
      throws IOException, ServletException {

    HttpServletRequest req = (HttpServletRequest) request;

    String authHeader = req.getHeader("Authorization");

    if (authHeader == null || !authHeader.startsWith("Bearer ")) {
      chain.doFilter(request, response);
      return;
    }

    String token = authHeader.substring(7);
    String email = jwtService.extractEmail(token);

    if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
      User user = userRepository.findByEmail(email).orElse(null);

      if (user != null && jwtService.isTokenValid(token, email)) {
        UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(user, null,
            user.getAuthorities());
        authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(req));
        SecurityContextHolder.getContext().setAuthentication(authentication);
      }
    }

    chain.doFilter(request, response);
  }
}
