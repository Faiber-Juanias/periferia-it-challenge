package com.fjuanias.periferiait.logicservice.infrastructure.adapter.out.persistence;

import java.util.List;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/** Repositorio Spring Data para publicaciones. */
public interface PostJpaRepository extends JpaRepository<PostJpaEntity, UUID> {

  /** Total de likes de una publicación vía la función PL/pgSQL fn_post_like_count. */
  @Query(value = "SELECT social.fn_post_like_count(:postId)", nativeQuery = true)
  long countLikes(@Param("postId") UUID postId);

  /**
   * Lista las publicaciones con su total de likes usando la función PL/pgSQL
   * social.fn_post_like_count. Aliases entre comillas para respetar el
   * mapeo de la proyección basada en interfaz.
   */
  @Query(value = """
      SELECT p.id            AS "id",
             p.author_id     AS "authorId",
             p.author_alias  AS "authorAlias",
             p.message       AS "message",
             p.created_at    AS "createdAt",
             social.fn_post_like_count(p.id) AS "likeCount"
      FROM social.posts p
      ORDER BY p.created_at DESC
      """, nativeQuery = true)
  List<PostWithLikesView> findAllWithLikes();
}
