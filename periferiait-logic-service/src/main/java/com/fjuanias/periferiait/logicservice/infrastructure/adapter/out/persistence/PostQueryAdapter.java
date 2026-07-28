package com.fjuanias.periferiait.logicservice.infrastructure.adapter.out.persistence;

import com.fjuanias.periferiait.logicservice.application.port.out.LoadPostsPort;
import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.util.List;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/** Adaptador de salida de solo lectura para publicaciones. */
@Component
public class PostQueryAdapter implements LoadPostsPort {

  private final PostJpaRepository repository;

  public PostQueryAdapter(PostJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  @Transactional(readOnly = true)
  public List<Post> loadAll() {
    return repository.findAllWithLikes().stream()
        .map(PostPersistenceMapper::toDomain)
        .toList();
  }
}
