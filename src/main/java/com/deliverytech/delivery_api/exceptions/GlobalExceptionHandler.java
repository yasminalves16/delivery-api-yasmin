package com.deliverytech.delivery_api.exceptions;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;

@RestControllerAdvice
public class GlobalExceptionHandler {

  @ExceptionHandler(EntityNotFoundException.class)
  public ResponseEntity<String> handleNotFound(EntityNotFoundException ex) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<String> handleBusiness(BusinessException ex) {
    return ResponseEntity.badRequest().body(ex.getMessage());
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<Map<String, String>> handleValidationErrors(
      MethodArgumentNotValidException ex) {

    Map<String, String> errors = new HashMap<>();

    ex.getBindingResult()
        .getFieldErrors()
        .forEach(error -> errors.put(error.getField(), error.getDefaultMessage()));

    return ResponseEntity.badRequest().body(errors);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<Map<String, String>> handleJsonParseError(HttpMessageNotReadableException ex) {
    Map<String, String> error = new HashMap<>();

    Throwable cause = ex.getMostSpecificCause();
    if (cause instanceof InvalidFormatException invalid) {
      String field = invalid.getPath().stream()
          .map(JsonMappingException.Reference::getFieldName)
          .reduce((a, b) -> a + "." + b)
          .orElse("desconhecido");
      error.put("error", "Campo inválido: " + field + " (tipo esperado: " +
          invalid.getTargetType().getSimpleName() + ")");
      return ResponseEntity.badRequest().body(error);
    }

    if (cause instanceof MismatchedInputException mismatched) {
      String field = mismatched.getPath().stream()
          .map(JsonMappingException.Reference::getFieldName)
          .reduce((a, b) -> a + "." + b)
          .orElse("desconhecido");
      error.put("error", "Campo inválido: " + field + " (tipo esperado: " +
          mismatched.getTargetType().getSimpleName() + ")");
      return ResponseEntity.badRequest().body(error);
    }

    error.put("error", "JSON inválido: verifique tipos dos campos.");
    return ResponseEntity.badRequest().body(error);
  }
}