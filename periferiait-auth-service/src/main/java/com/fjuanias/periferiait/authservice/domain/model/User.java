package com.fjuanias.periferiait.authservice.domain.model;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.UUID;

/**
 * Modelo de dominio puro (sin dependencias de frameworks).
 * Representa a un usuario de la red social junto con su perfil.
 */
public record User(
    UUID id,
    String username,
    String passwordHash,
    String firstName,
    String lastName,
    LocalDate birthDate,
    String alias,
    OffsetDateTime createdAt
) {
}
