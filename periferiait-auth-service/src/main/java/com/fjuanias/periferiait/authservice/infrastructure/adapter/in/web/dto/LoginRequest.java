package com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;

/** Cuerpo del request de login. */
public record LoginRequest(
    @NotBlank(message = "El usuario es obligatorio") String username,
    @NotBlank(message = "La contraseña es obligatoria") String password
) {
}
