package com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web.dto;

import com.fjuanias.periferiait.authservice.domain.model.User;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Perfil del usuario autenticado (sin exponer el hash de contraseña). */
public record ProfileResponse(
    UUID id,
    String username,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String alias,
    OffsetDateTime createdAt
) {

  public static ProfileResponse from(User user) {
    return new ProfileResponse(
        user.id(),
        user.username(),
        user.firstName(),
        user.lastName(),
        user.birthDate(),
        user.alias(),
        user.createdAt()
    );
  }
}
