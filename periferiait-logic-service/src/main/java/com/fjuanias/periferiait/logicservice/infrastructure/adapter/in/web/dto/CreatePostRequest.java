package com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

/** Cuerpo del request para crear una publicación (solo el mensaje). */
public record CreatePostRequest(
    @NotBlank(message = "El mensaje es obligatorio")
    @Size(max = 1000, message = "El mensaje no puede superar 1000 caracteres")
    String message
) {
}
