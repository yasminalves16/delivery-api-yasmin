package com.deliverytech.delivery_api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.deliverytech.delivery_api.security.CustomAccessDeniedHandler;
import com.deliverytech.delivery_api.security.CustomAuthenticationEntryPoint;
import com.deliverytech.delivery_api.security.JwtAuthenticationFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {
  private final JwtAuthenticationFilter jwtFilter;
  private final CustomAccessDeniedHandler accessDeniedHandler;
  private final CustomAuthenticationEntryPoint authenticationEntryPoint;

  public SecurityConfig(JwtAuthenticationFilter jwtFilter,
      CustomAccessDeniedHandler accessDeniedHandler,
      CustomAuthenticationEntryPoint authenticationEntryPoint) {
    this.jwtFilter = jwtFilter;
    this.accessDeniedHandler = accessDeniedHandler;
    this.authenticationEntryPoint = authenticationEntryPoint;
  }

  @Bean
  public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
    http.csrf(c -> c.disable())
        .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        .exceptionHandling(ex -> ex
            .authenticationEntryPoint(authenticationEntryPoint)
            .accessDeniedHandler(accessDeniedHandler))
        .authorizeHttpRequests(auth -> auth
            .requestMatchers("/error").permitAll()
            .requestMatchers("/swagger-ui/**", "/api-docs/**", "/swagger-ui.html", "/h2-console/**").permitAll()
            .requestMatchers("/health", "/info").permitAll()

            .requestMatchers("/api/auth/admin/**", "/api/auth/me").authenticated()
            .requestMatchers("/api/auth/**").permitAll()

            .requestMatchers(HttpMethod.POST, "/customers").permitAll()

            .requestMatchers(HttpMethod.POST, "/restaurants").hasAnyRole("RESTAURANT", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/restaurants/*/toggle").hasAnyRole("RESTAURANT", "ADMIN")
            .requestMatchers("/restaurants/**").permitAll()

            .requestMatchers(HttpMethod.GET, "/products/all").hasAnyRole("RESTAURANT", "ADMIN")
            .requestMatchers(HttpMethod.GET, "/products", "/products/*", "/products/restaurant/**").permitAll()
            .requestMatchers(HttpMethod.POST, "/products/**").hasAnyRole("RESTAURANT", "ADMIN")
            .requestMatchers(HttpMethod.PATCH, "/products/**").hasAnyRole("RESTAURANT", "ADMIN")
            .requestMatchers(HttpMethod.PUT, "/products/**").hasAnyRole("RESTAURANT", "ADMIN")
            .requestMatchers(HttpMethod.DELETE, "/products/**").hasAnyRole("RESTAURANT", "ADMIN")

            .requestMatchers("/customer-orders/**", "/api/customer-orders/**").authenticated()
            .requestMatchers("/order-items/**", "/reports/**", "/customers/**").authenticated()

            .anyRequest().permitAll())
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class);

    http.headers(headers -> headers.frameOptions(frameOptions -> frameOptions.disable()));

    return http.build();
  }

  @Bean
  public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
    return config.getAuthenticationManager();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }
}