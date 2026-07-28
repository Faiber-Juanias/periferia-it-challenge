package com.fjuanias.periferiait.logicservice.application.service;

import com.fjuanias.periferiait.logicservice.application.port.in.ToggleLikeUseCase;
import com.fjuanias.periferiait.logicservice.application.port.out.LikeEventPublisherPort;
import com.fjuanias.periferiait.logicservice.application.port.out.ToggleLikePort;
import com.fjuanias.periferiait.logicservice.domain.model.LikeEvent;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Caso de uso de like: ejecuta el toggle (procedure) y difunde el nuevo total
 * por el puerto de eventos (WebSocket) para actualización en tiempo real.
 */
@Service
public class LikeService implements ToggleLikeUseCase {

  private static final Logger log = LoggerFactory.getLogger(LikeService.class);

  private final ToggleLikePort toggleLikePort;
  private final LikeEventPublisherPort likeEventPublisherPort;

  public LikeService(ToggleLikePort toggleLikePort,
                     LikeEventPublisherPort likeEventPublisherPort) {
    this.toggleLikePort = toggleLikePort;
    this.likeEventPublisherPort = likeEventPublisherPort;
  }

  @Override
  public LikeEvent toggle(UUID postId, UUID userId) {
    long likeCount = toggleLikePort.toggle(postId, userId);
    LikeEvent event = new LikeEvent(postId, likeCount);
    likeEventPublisherPort.publish(event);
    log.info("Like toggled post={} por user={} -> total={}", postId, userId, likeCount);
    return event;
  }
}
