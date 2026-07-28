package com.fjuanias.periferiait.logicservice.application.port.in;

import com.fjuanias.periferiait.logicservice.domain.model.Post;

/** Caso de uso: crear una publicación (la fecha la asigna la BD por defecto). */
public interface CreatePostUseCase {

  Post create(CreatePostCommand command);
}
