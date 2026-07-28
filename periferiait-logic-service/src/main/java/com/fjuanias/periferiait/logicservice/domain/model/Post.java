package com.fjuanias.periferiait.logicservice.domain.model;

import java.time.OffsetDateTime;
import java.util.UUID;

/** Modelo de dominio puro de una publicación con su total de likes. */
public record Post(
    UUID id,
    UUID authorId,
    String authorAlias,
    String message,
    OffsetDateTime createdAt,
    long likeCount
) {
}
