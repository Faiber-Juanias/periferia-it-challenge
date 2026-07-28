package com.fjuanias.periferiait.logicservice.infrastructure.adapter.out.persistence;

import com.fjuanias.periferiait.logicservice.application.port.out.CreatePostPort;
import com.fjuanias.periferiait.logicservice.application.port.out.ToggleLikePort;
import com.fjuanias.periferiait.logicservice.domain.model.Post;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.StoredProcedureQuery;
import java.util.UUID;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * Adaptador de salida que invoca los procedimientos PL/pgSQL de social.
 * Ambos procedimientos usan solo parámetros IN (sin OUT/INOUT), porque pgjdbc
 * no soporta extraer parámetros de salida UUID en un CALL de procedimiento.
 *   - sp_create_post : inserta con el id generado por la app.
 *   - sp_toggle_like : da/quita like; el total se lee con fn_post_like_count.
 */
@Component
public class SocialProcedureAdapter implements CreatePostPort, ToggleLikePort {

  @PersistenceContext
  private EntityManager entityManager;

  private final PostJpaRepository repository;

  public SocialProcedureAdapter(PostJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional
  public Post create(UUID authorId, String authorAlias, String message) {
    UUID newId = UUID.randomUUID();

    StoredProcedureQuery query = entityManager
        .createStoredProcedureQuery("social.sp_create_post");
    query.registerStoredProcedureParameter("p_id", UUID.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_author_id", UUID.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_author_alias", String.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_message", String.class, ParameterMode.IN);

    query.setParameter("p_id", newId);
    query.setParameter("p_author_id", authorId);
    query.setParameter("p_author_alias", authorAlias);
    query.setParameter("p_message", message);
    query.execute();

    PostJpaEntity entity = repository.findById(newId)
        .orElseThrow(() -> new IllegalStateException("No se pudo recuperar la publicación creada"));
    return PostPersistenceMapper.toDomain(entity, 0L);
  }

  @Override
  @Transactional
  public long toggle(UUID postId, UUID userId) {
    StoredProcedureQuery query = entityManager
        .createStoredProcedureQuery("social.sp_toggle_like");
    query.registerStoredProcedureParameter("p_post_id", UUID.class, ParameterMode.IN);
    query.registerStoredProcedureParameter("p_user_id", UUID.class, ParameterMode.IN);
    query.setParameter("p_post_id", postId);
    query.setParameter("p_user_id", userId);
    query.execute();

    // El total actualizado se obtiene con la función (dentro de la misma tx).
    return repository.countLikes(postId);
  }
}
