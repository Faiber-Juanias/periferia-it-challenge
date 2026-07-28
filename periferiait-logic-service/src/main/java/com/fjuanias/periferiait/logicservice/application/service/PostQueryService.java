package com.fjuanias.periferiait.logicservice.application.service;

import com.fjuanias.periferiait.logicservice.application.port.in.ListPostsUseCase;
import com.fjuanias.periferiait.logicservice.application.port.out.LoadPostsPort;
import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.util.List;
import org.springframework.stereotype.Service;

/** Caso de uso de consulta de publicaciones. */
@Service
public class PostQueryService implements ListPostsUseCase {

  private final LoadPostsPort loadPostsPort;

  public PostQueryService(LoadPostsPort loadPostsPort) {
    this.loadPostsPort = loadPostsPort;
  }

  @Override
  public List<Post> listAll() {
    return loadPostsPort.loadAll();
  }
}
