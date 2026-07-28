export interface Post {
  id: string;
  authorId: string;
  authorAlias: string;
  message: string;
  createdAt: string;
  likeCount: number;
}

export interface CreatePostRequest {
  message: string;
}

/** Respuesta de POST /posts/{id}/likes y payload del evento WebSocket. */
export interface LikeEvent {
  postId: string;
  likeCount: number;
}
