package com.fjuanias.periferiait.authservice.infrastructure.security.jwt;

import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * Propiedades del JWT (prefijo security.jwt en application.yaml).
 * La 'secret' debe coincidir con la del logic-service para validar HS256.
 */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String secret, long expirationSeconds, String issuer) {
}
