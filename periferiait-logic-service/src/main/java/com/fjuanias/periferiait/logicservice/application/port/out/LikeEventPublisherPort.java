package com.fjuanias.periferiait.logicservice.application.port.out;

import com.fjuanias.periferiait.logicservice.domain.model.LikeEvent;

/** Puerto de salida para difundir eventos de like (implementado con WebSocket). */
public interface LikeEventPublisherPort {

  void publish(LikeEvent event);
}
