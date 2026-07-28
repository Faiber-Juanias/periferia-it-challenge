package com.fjuanias.periferiait.logicservice.application.port.in;

import java.util.UUID;

/** Datos de entrada para crear una publicación. El autor viene del JWT. */
public record CreatePostCommand(UUID authorId, String authorAlias, String message) {
}
