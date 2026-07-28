package com.fjuanias.periferiait.logicservice.infrastructure.adapter.out.persistence;

import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.time.ZoneOffset;

/** Traduce entre estructuras de persistencia y el modelo de dominio. */
final class PostPersistenceMapper {

  private PostPersistenceMapper() {
  }

  static Post toDomain(PostWithLikesView view) {
    return new Post(
        view.getId(),
        view.getAuthorId(),
        view.getAuthorAlias(),
        view.getMessage(),
        view.getCreatedAt().atOffset(ZoneOffset.UTC),
        view.getLikeCount()
    );
  }

  static Post toDomain(PostJpaEntity entity, long likeCount) {
    return new Post(
        entity.getId(),
        entity.getAuthorId(),
        entity.getAuthorAlias(),
        entity.getMessage(),
        entity.getCreatedAt(),
        likeCount
    );
  }
}
