import { RenderMode, ServerRoute } from '@angular/ssr';

export const serverRoutes: ServerRoute[] = [
  {
    // App con autenticación + WebSocket: rendering en cliente (no prerender/SSR),
    // así el código de navegador (localStorage, SockJS) no se ejecuta en build.
    path: '**',
    renderMode: RenderMode.Client
  }
];
