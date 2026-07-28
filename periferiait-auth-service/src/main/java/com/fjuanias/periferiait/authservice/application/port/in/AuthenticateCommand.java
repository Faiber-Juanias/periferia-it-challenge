package com.fjuanias.periferiait.authservice.application.port.in;

/** Datos de entrada para el caso de uso de autenticación. */
public record AuthenticateCommand(String username, String rawPassword) {
}
