package com.fjuanias.periferiait.logicservice.application.port.in;

import com.fjuanias.periferiait.logicservice.domain.model.LikeEvent;
import java.util.UUID;

/** Caso de uso: dar/quitar like a una publicación y difundir el nuevo total. */
public interface ToggleLikeUseCase {

  LikeEvent toggle(UUID postId, UUID userId);
}
