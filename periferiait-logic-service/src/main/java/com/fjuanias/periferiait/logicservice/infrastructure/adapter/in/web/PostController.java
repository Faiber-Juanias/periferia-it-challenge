package com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web;

import com.fjuanias.periferiait.logicservice.application.port.in.CreatePostCommand;
import com.fjuanias.periferiait.logicservice.application.port.in.CreatePostUseCase;
import com.fjuanias.periferiait.logicservice.application.port.in.ListPostsUseCase;
import com.fjuanias.periferiait.logicservice.application.port.in.ToggleLikeUseCase;
import com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web.dto.CreatePostRequest;
import com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web.dto.LikeResponse;
import com.fjuanias.periferiait.logicservice.infrastructure.adapter.in.web.dto.PostResponse;
import com.fjuanias.periferiait.logicservice.infrastructure.security.AuthenticatedUser;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Adaptador de entrada REST para publicaciones y likes. */
@RestController
@RequestMapping("/posts")
public class PostController {

  private final ListPostsUseCase listPostsUseCase;
  private final CreatePostUseCase createPostUseCase;
  private final ToggleLikeUseCase toggleLikeUseCase;

  public PostController(ListPostsUseCase listPostsUseCase,
                        CreatePostUseCase createPostUseCase,
                        ToggleLikeUseCase toggleLikeUseCase) {
    this.listPostsUseCase = listPostsUseCase;
    this.createPostUseCase = createPostUseCase;
    this.toggleLikeUseCase = toggleLikeUseCase;
  }

  @GetMapping
  public ResponseEntity<List<PostResponse>> list() {
    List<PostResponse> posts = listPostsUseCase.listAll().stream()
        .map(PostResponse::from)
        .toList();
    return ResponseEntity.ok(posts);
  }

  @PostMapping
  public ResponseEntity<PostResponse> create(@Valid @RequestBody CreatePostRequest request,
                                             @AuthenticationPrincipal AuthenticatedUser user) {
    PostResponse response = PostResponse.from(createPostUseCase.create(
        new CreatePostCommand(user.id(), user.alias(), request.message())));
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/{postId}/likes")
  public ResponseEntity<LikeResponse> toggleLike(@PathVariable UUID postId,
                                                 @AuthenticationPrincipal AuthenticatedUser user) {
    LikeResponse response = LikeResponse.from(toggleLikeUseCase.toggle(postId, user.id()));
    return ResponseEntity.ok(response);
  }
}
