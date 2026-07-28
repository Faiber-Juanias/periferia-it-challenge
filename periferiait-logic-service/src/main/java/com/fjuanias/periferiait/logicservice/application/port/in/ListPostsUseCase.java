package com.fjuanias.periferiait.logicservice.application.port.in;

import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.util.List;

/** Caso de uso: listar publicaciones con su total de likes. */
public interface ListPostsUseCase {

  List<Post> listAll();
}
