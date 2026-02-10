package com.deliverytech.delivery_api.exceptions;

import java.util.List;

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
  public ResponseEntity<ErrorResponse> handleNotFound(EntityNotFoundException ex) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.NOT_FOUND.value(),
        ex.getMessage(),
        System.currentTimeMillis(),
        null);
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponse> handleBusiness(BusinessException ex) {
    ErrorResponse error = new ErrorResponse(
        HttpStatus.CONFLICT.value(),
        ex.getMessage(),
        System.currentTimeMillis(),
        null);
    return ResponseEntity.status(HttpStatus.CONFLICT).body(error);
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponse> handleValidationErrors(
      MethodArgumentNotValidException ex) {
    List<ErrorResponse.ValidationError> errors = ex.getBindingResult()
        .getFieldErrors()
        .stream()
        .map(error -> new ErrorResponse.ValidationError(
            error.getField(),
            error.getDefaultMessage()))
        .toList();

    ErrorResponse response = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        "Erro de validacao nos campos informados.",
        System.currentTimeMillis(),
        List.copyOf(errors));

    return ResponseEntity.badRequest().body(response);
  }

  @ExceptionHandler(HttpMessageNotReadableException.class)
  public ResponseEntity<ErrorResponse> handleJsonParseError(HttpMessageNotReadableException ex) {
    String message = "JSON inválido: verifique tipos dos campos.";

    Throwable cause = ex.getMostSpecificCause();
    if (cause instanceof InvalidFormatException invalid) {
      String field = invalid.getPath().stream()
          .map(JsonMappingException.Reference::getFieldName)
          .reduce((a, b) -> a + "." + b)
          .orElse("desconhecido");
      message = "Campo inválido: " + field + " (tipo esperado: " +
          invalid.getTargetType().getSimpleName() + ")";
    } else if (cause instanceof MismatchedInputException mismatched) {
      String field = mismatched.getPath().stream()
          .map(JsonMappingException.Reference::getFieldName)
          .reduce((a, b) -> a + "." + b)
          .orElse("desconhecido");
      message = "Campo inválido: " + field + " (tipo esperado: " +
          mismatched.getTargetType().getSimpleName() + ")";
    }

    ErrorResponse error = new ErrorResponse(
        HttpStatus.BAD_REQUEST.value(),
        message,
        System.currentTimeMillis(),
        null);
    return ResponseEntity.badRequest().body(error);
  }
}