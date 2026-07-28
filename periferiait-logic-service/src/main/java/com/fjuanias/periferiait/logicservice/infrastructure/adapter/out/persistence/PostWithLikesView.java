package com.fjuanias.periferiait.logicservice.infrastructure.adapter.out.persistence;

import java.time.Instant;
import java.util.UUID;

/**
 * Proyección de una publicación junto con su total de likes (fn_post_like_count).
 * createdAt se declara como Instant porque una interface projection sobre un query
 * nativo recibe el valor crudo del ResultSet (timestamptz -> Instant), sin pasar
 * por el sistema de tipos de Hibernate. El mapper lo convierte al tipo del dominio.
 */
public interface PostWithLikesView {

  UUID getId();

  UUID getAuthorId();

  String getAuthorAlias();

  String getMessage();

  Instant getCreatedAt();

  long getLikeCount();
}
