package com.fjuanias.periferiait.logicservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fjuanias.periferiait.logicservice.application.port.out.LikeEventPublisherPort;
import com.fjuanias.periferiait.logicservice.application.port.out.ToggleLikePort;
import com.fjuanias.periferiait.logicservice.domain.model.LikeEvent;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class LikeServiceTest {

  @Mock
  private ToggleLikePort toggleLikePort;
  @Mock
  private LikeEventPublisherPort likeEventPublisherPort;
  @InjectMocks
  private LikeService service;

  @Test
  void toggle_returnsUpdatedCount_andBroadcastsEvent() {
    UUID postId = UUID.randomUUID();
    UUID userId = UUID.randomUUID();
    when(toggleLikePort.toggle(postId, userId)).thenReturn(5L);

    LikeEvent event = service.toggle(postId, userId);

    assertThat(event.postId()).isEqualTo(postId);
    assertThat(event.likeCount()).isEqualTo(5L);
    // El total se difunde por el puerto de eventos (WebSocket) para tiempo real.
    verify(likeEventPublisherPort).publish(event);
  }
}
