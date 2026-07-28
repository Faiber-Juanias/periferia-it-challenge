package com.fjuanias.periferiait.authservice.application.port.out;

/** Token generado por el proveedor de tokens junto con su vigencia. */
public record GeneratedToken(String value, long expiresInSeconds) {
}
