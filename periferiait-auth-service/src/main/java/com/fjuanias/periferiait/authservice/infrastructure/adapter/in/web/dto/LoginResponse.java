package com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web.dto;

import com.fjuanias.periferiait.authservice.application.port.in.AuthenticationResult;
import java.util.UUID;

/** Respuesta del login con el token JWT emitido. */
public record LoginResponse(
    String accessToken,
    String tokenType,
    long expiresIn,
    UUID userId,
    String username,
    String alias
) {

  public static LoginResponse from(AuthenticationResult result) {
    return new LoginResponse(
        result.token(),
        "Bearer",
        result.expiresInSeconds(),
        result.userId(),
        result.username(),
        result.alias()
    );
  }
}
