import { isPlatformBrowser } from '@angular/common';
import { Injectable, PLATFORM_ID, inject } from '@angular/core';
import { Client } from '@stomp/stompjs';
import SockJS from 'sockjs-client';
import { API_CONFIG } from '../config/api.config';
import { LikeEvent } from '../models/post.model';

/**
 * Cliente STOMP sobre WebSocket (SockJS) para recibir los likes en tiempo real.
 * Solo actúa en el navegador; en el servidor (SSR) es un no-op.
 */
@Injectable({ providedIn: 'root' })
export class RealtimeService {
  private readonly platformId = inject(PLATFORM_ID);
  private client: Client | null = null;

  /** Se conecta a /ws y se suscribe a /topic/posts.likes. */
  connect(onLikeEvent: (event: LikeEvent) => void): void {
    if (!isPlatformBrowser(this.platformId) || this.client?.active) {
      return;
    }

    this.client = new Client({
      webSocketFactory: () => new SockJS(API_CONFIG.wsUrl),
      reconnectDelay: 3000,
      onConnect: () => {
        this.client?.subscribe('/topic/posts.likes', (message) => {
          onLikeEvent(JSON.parse(message.body) as LikeEvent);
        });
      },
    });

    this.client.activate();
  }

  disconnect(): void {
    this.client?.deactivate();
    this.client = null;
  }
}
