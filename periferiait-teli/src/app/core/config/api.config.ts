/**
 * URLs de los microservicios. Para producción/Docker se pueden sobrescribir
 * (p. ej. sirviendo un config.json en runtime o vía variables de build).
 */
export const API_CONFIG = {
  authBaseUrl: 'http://localhost:8081',
  logicBaseUrl: 'http://localhost:8082',
  wsUrl: 'http://localhost:8082/ws',
} as const;
