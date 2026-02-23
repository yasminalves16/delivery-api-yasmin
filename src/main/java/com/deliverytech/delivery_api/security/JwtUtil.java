package com.deliverytech.delivery_api.security;

import java.nio.charset.StandardCharsets;
import java.util.Date;

import javax.crypto.SecretKey;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

@Component
public class JwtUtil {
  private String secret;

  private Long expiration;

  private SecretKey getSigningKey() {
    return Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public String generateToken(String email) {
    Date now = new Date();
    Date validity = new Date(now.getTime() + expiration);

    return Jwts.builder()
        .setSubject(email)
        .setIssuedAt(now)
        .setExpiration(validity)
        .signWith(getSigningKey())
        .compact();
  }

  public boolean isTokenValid(String token, UserDetails userDetails) {
    final String email = getEmail(token);
    return email.equals(userDetails.getUsername());
  }

  public String getEmail(String token) {
    return Jwts.parserBuilder()
        .setSigningKey(getSigningKey())
        .build()
        .parseClaimsJws(token)
        .getBody()
        .getSubject();
  }
}