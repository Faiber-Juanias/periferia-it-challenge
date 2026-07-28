package com.fjuanias.periferiait.authservice.application.port.in;

import java.util.UUID;

/** Resultado del caso de uso de autenticación. */
public record AuthenticationResult(
    String token,
    long expiresInSeconds,
    UUID userId,
    String username,
    String alias
) {
}
