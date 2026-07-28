import { DatePipe } from '@angular/common';
import { Component, OnDestroy, OnInit, inject, signal } from '@angular/core';
import { NonNullableFormBuilder, ReactiveFormsModule, Validators } from '@angular/forms';
import { AuthStore } from '../../core/auth/auth.store';
import { PostsStore } from '../../core/posts/posts.store';
import { PostService } from '../../core/services/post.service';
import { RealtimeService } from '../../core/services/realtime.service';

@Component({
  selector: 'app-posts',
  imports: [ReactiveFormsModule, DatePipe],
  templateUrl: './posts.html',
})
export class Posts implements OnInit, OnDestroy {
  private readonly fb = inject(NonNullableFormBuilder);
  private readonly postService = inject(PostService);
  private readonly realtime = inject(RealtimeService);
  protected readonly postsStore = inject(PostsStore);
  protected readonly authStore = inject(AuthStore);

  protected readonly publishing = signal(false);

  protected readonly form = this.fb.group({
    message: ['', [Validators.required, Validators.maxLength(1000)]],
  });

  ngOnInit(): void {
    this.postsStore.load();
    // Likes en tiempo real: cada evento actualiza el contador en el estado.
    this.realtime.connect((event) => this.postsStore.applyLike(event));
  }

  ngOnDestroy(): void {
    this.realtime.disconnect();
  }

  publish(): void {
    if (this.form.invalid) {
      this.form.markAllAsTouched();
      return;
    }

    this.publishing.set(true);
    this.postService.createPost(this.form.getRawValue().message).subscribe({
      next: (post) => {
        this.postsStore.prepend(post);
        this.form.reset();
        this.publishing.set(false);
      },
      error: () => this.publishing.set(false),
    });
  }

  like(postId: string): void {
    // La respuesta actualiza el contador de inmediato; el evento WebSocket
    // hará lo propio en los demás clientes conectados.
    this.postService.toggleLike(postId).subscribe({
      next: (event) => this.postsStore.applyLike(event),
    });
  }
}
