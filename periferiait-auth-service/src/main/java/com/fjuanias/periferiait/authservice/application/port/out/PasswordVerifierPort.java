package com.fjuanias.periferiait.authservice.application.port.out;

/** Puerto de salida para verificar contraseñas contra su hash almacenado. */
public interface PasswordVerifierPort {

  boolean matches(String rawPassword, String passwordHash);
}
