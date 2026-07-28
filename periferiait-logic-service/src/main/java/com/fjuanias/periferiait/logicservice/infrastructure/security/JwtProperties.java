package com.fjuanias.periferiait.logicservice.infrastructure.security;

import org.springframework.boot.context.properties.ConfigurationProperties;

/** Secret HS256 compartida con auth-service para validar los tokens entrantes. */
@ConfigurationProperties(prefix = "security.jwt")
public record JwtProperties(String secret) {
}
