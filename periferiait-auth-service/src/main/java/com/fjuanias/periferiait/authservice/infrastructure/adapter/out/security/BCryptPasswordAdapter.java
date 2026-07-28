package com.fjuanias.periferiait.authservice.infrastructure.adapter.out.security;

import com.fjuanias.periferiait.authservice.application.port.out.PasswordVerifierPort;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

/** Adaptador de salida que verifica contraseñas con BCrypt. */
@Component
public class BCryptPasswordAdapter implements PasswordVerifierPort {

  private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

  @Override
  public boolean matches(String rawPassword, String passwordHash) {
    return passwordEncoder.matches(rawPassword, passwordHash);
  }
}
