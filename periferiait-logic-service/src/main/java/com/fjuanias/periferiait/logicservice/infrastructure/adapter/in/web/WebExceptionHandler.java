package com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web;

import java.time.OffsetDateTime;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/** Manejo centralizado de errores con respuestas consistentes. */
@RestControllerAdvice
public class WebExceptionHandler {

  private static final Logger log = LoggerFactory.getLogger(WebExceptionHandler.class);

  public record ApiError(OffsetDateTime timestamp, int status, String error, String message) {

    static ApiError of(HttpStatus status, String message) {
      return new ApiError(OffsetDateTime.now(), status.value(), status.getReasonPhrase(), message);
    }
  }

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ApiError> handleValidation(MethodArgumentNotValidException ex) {
    String message = ex.getBindingResult().getFieldErrors().stream()
        .map(err -> err.getField() + ": " + err.getDefaultMessage())
        .collect(Collectors.joining("; "));
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiError.of(HttpStatus.BAD_REQUEST, message));
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ApiError> handleIntegrity(DataIntegrityViolationException ex) {
    log.warn("Violación de integridad: {}", ex.getMessage());
    return ResponseEntity.status(HttpStatus.BAD_REQUEST)
        .body(ApiError.of(HttpStatus.BAD_REQUEST,
            "La operación viola una restricción de datos (¿la publicación existe?)"));
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<ApiError> handleGeneric(Exception ex) {
    log.error("Error no controlado", ex);
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
        .body(ApiError.of(HttpStatus.INTERNAL_SERVER_ERROR, "Error interno del servidor"));
  }
}
