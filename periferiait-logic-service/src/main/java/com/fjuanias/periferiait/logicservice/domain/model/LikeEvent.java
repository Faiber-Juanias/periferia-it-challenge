package com.fjuanias.periferiait.logicservice.domain.model;

import java.util.UUID;

/**
 * Evento de dominio: un post cambió su total de likes.
 * Se devuelve al dar like y se difunde por WebSocket en tiempo real.
 */
public record LikeEvent(UUID postId, long likeCount) {
}
