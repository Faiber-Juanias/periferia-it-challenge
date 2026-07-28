package com.fjuanias.periferiait.logicservice.infrastructure.adapter.out.messaging;

import com.fjuanias.periferiait.logicservice.application.port.out.LikeEventPublisherPort;
import com.fjuanias.periferiait.logicservice.domain.model.LikeEvent;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

/**
 * Adaptador de salida que difunde los eventos de like por STOMP.
 * Todos los clientes suscritos a /topic/posts.likes reciben el nuevo total.
 */
@Component
public class LikeEventPublisherAdapter implements LikeEventPublisherPort {

  public static final String LIKES_TOPIC = "/topic/posts.likes";

  private final SimpMessagingTemplate messagingTemplate;

  public LikeEventPublisherAdapter(SimpMessagingTemplate messagingTemplate) {
    this.messagingTemplate = messagingTemplate;
  }

  @Override
  public void publish(LikeEvent event) {
    messagingTemplate.convertAndSend(LIKES_TOPIC, event);
  }
}
