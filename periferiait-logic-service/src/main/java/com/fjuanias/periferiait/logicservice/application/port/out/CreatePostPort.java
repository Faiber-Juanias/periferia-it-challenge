package com.fjuanias.periferiait.logicservice.application.port.out;

import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.util.UUID;

/** Puerto de salida para persistir una publicación (vía procedure sp_create_post). */
public interface CreatePostPort {

  Post create(UUID authorId, String authorAlias, String message);
}
