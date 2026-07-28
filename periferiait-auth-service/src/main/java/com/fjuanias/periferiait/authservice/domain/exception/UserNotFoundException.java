package com.fjuanias.periferiait.authservice.domain.exception;

import java.util.UUID;

/** Se lanza cuando no existe un usuario con el identificador dado. */
public class UserNotFoundException extends RuntimeException {

  public UserNotFoundException(UUID userId) {
    super("No se encontró el usuario con id " + userId);
  }
}
