package com.fjuanias.periferiait.logicservice.infrastructure.adapter.out.persistence;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.OffsetDateTime;
import java.util.UUID;
import lombok.Getter;
import lombok.Setter;

/** Entidad JPA mapeada a social.posts (creada por database/init.sql). */
@Entity
@Table(name = "posts", schema = "social")
@Getter
@Setter
public class PostJpaEntity {

  @Id
  @Column(name = "id", nullable = false, updatable = false)
  private UUID id;

  @Column(name = "author_id", nullable = false)
  private UUID authorId;

  @Column(name = "author_alias", nullable = false)
  private String authorAlias;

  @Column(name = "message", nullable = false)
  private String message;

  @Column(name = "created_at", nullable = false)
  private OffsetDateTime createdAt;
}
