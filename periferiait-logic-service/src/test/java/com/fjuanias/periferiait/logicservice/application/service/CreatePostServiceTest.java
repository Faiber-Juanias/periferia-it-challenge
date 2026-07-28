package com.fjuanias.periferiait.logicservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fjuanias.periferiait.logicservice.application.port.in.CreatePostCommand;
import com.fjuanias.periferiait.logicservice.application.port.out.CreatePostPort;
import com.fjuanias.periferiait.logicservice.domain.model.Post;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class CreatePostServiceTest {

  @Mock
  private CreatePostPort createPostPort;
  @InjectMocks
  private CreatePostService service;

  @Test
  void create_delegatesToPort_andReturnsCreatedPost() {
    UUID authorId = UUID.randomUUID();
    Post created = new Post(UUID.randomUUID(), authorId, "juanp", "hola", OffsetDateTime.now(), 0L);
    when(createPostPort.create(authorId, "juanp", "hola")).thenReturn(created);

    Post result = service.create(new CreatePostCommand(authorId, "juanp", "hola"));

    assertThat(result).isEqualTo(created);
    verify(createPostPort).create(authorId, "juanp", "hola");
  }
}
