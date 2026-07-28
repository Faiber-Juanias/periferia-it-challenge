package com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web.dto;

import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.time.OffsetDateTime;
import java.util.UUID;

/** Representación de salida de una publicación. */
public record PostResponse(
    UUID id,
    UUID authorId,
    String authorAlias,
    String message,
    OffsetDateTime createdAt,
    long likeCount
) {

  public static PostResponse from(Post post) {
    return new PostResponse(
        post.id(),
        post.authorId(),
        post.authorAlias(),
        post.message(),
        post.createdAt(),
        post.likeCount()
    );
  }
}
