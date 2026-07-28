package com.fjuanias.periferiait.logicservice.application.service;

import com.fjuanias.periferiait.logicservice.application.port.in.CreatePostCommand;
import com.fjuanias.periferiait.logicservice.application.port.in.CreatePostUseCase;
import com.fjuanias.periferiait.logicservice.application.port.out.CreatePostPort;
import com.fjuanias.periferiait.logicservice.domain.model.Post;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/** Caso de uso de creación de publicaciones. */
@Service
public class CreatePostService implements CreatePostUseCase {

  private static final Logger log = LoggerFactory.getLogger(CreatePostService.class);

  private final CreatePostPort createPostPort;

  public CreatePostService(CreatePostPort createPostPort) {
    this.createPostPort = createPostPort;
  }

  @Override
  public Post create(CreatePostCommand command) {
    Post post = createPostPort.create(
        command.authorId(), command.authorAlias(), command.message());
    log.info("Publicación creada id={} por alias={}", post.id(), post.authorAlias());
    return post;
  }
}
