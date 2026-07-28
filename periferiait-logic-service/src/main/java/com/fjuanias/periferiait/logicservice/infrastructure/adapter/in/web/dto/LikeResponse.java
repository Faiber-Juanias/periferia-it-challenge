package com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web.dto;

import com.fjuanias.periferiait.logicservice.domain.model.LikeEvent;
import java.util.UUID;

/** Respuesta al dar/quitar like: total actualizado de la publicación. */
public record LikeResponse(UUID postId, long likeCount) {

  public static LikeResponse from(LikeEvent event) {
    return new LikeResponse(event.postId(), event.likeCount());
  }
}
