package com.fjuanias.periferiait.logicservice.application.port.out;

import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.util.List;

/** Puerto de salida para leer publicaciones con su total de likes. */
public interface LoadPostsPort {

  List<Post> loadAll();
}
