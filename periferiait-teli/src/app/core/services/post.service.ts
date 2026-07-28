import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_CONFIG } from '../config/api.config';
import { LikeEvent, Post } from '../models/post.model';

/** Cliente HTTP del logic-service (publicaciones + likes). */
@Injectable({ providedIn: 'root' })
export class PostService {
  private readonly http = inject(HttpClient);

  getPosts(): Observable<Post[]> {
    return this.http.get<Post[]>(`${API_CONFIG.logicBaseUrl}/posts`);
  }

  createPost(message: string): Observable<Post> {
    return this.http.post<Post>(`${API_CONFIG.logicBaseUrl}/posts`, { message });
  }

  toggleLike(postId: string): Observable<LikeEvent> {
    return this.http.post<LikeEvent>(`${API_CONFIG.logicBaseUrl}/posts/${postId}/likes`, {});
  }
}
