import { inject } from '@angular/core';
import {
  patchState,
  signalStore,
  withMethods,
  withState,
} from '@ngrx/signals';
import { LikeEvent, Post } from '../models/post.model';
import { PostService } from '../services/post.service';

interface PostsState {
  posts: Post[];
  loading: boolean;
  error: string | null;
}

const initialState: PostsState = {
  posts: [],
  loading: false,
  error: null,
};

/**
 * Estado de publicaciones (NgRx SignalStore). Carga la lista, agrega las nuevas
 * y aplica los eventos de like recibidos por WebSocket (tiempo real).
 */
export const PostsStore = signalStore(
  { providedIn: 'root' },
  withState(initialState),
  withMethods((store) => {
    const postService = inject(PostService);

    return {
      load(): void {
        patchState(store, { loading: true, error: null });
        postService.getPosts().subscribe({
          next: (posts) => patchState(store, { posts, loading: false }),
          error: () =>
            patchState(store, {
              error: 'No se pudieron cargar las publicaciones',
              loading: false,
            }),
        });
      },
      prepend(post: Post): void {
        patchState(store, { posts: [post, ...store.posts()] });
      },
      applyLike(event: LikeEvent): void {
        patchState(store, {
          posts: store.posts().map((post) =>
            post.id === event.postId ? { ...post, likeCount: event.likeCount } : post,
          ),
        });
      },
    };
  }),
);
