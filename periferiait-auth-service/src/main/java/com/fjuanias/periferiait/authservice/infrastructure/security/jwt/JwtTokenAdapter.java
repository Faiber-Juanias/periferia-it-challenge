package com.fjuanias.periferiait.authservice.infrastructure.security.jwt;

import com.fjuanias.periferiait.authservice.application.port.out.GeneratedToken;
import com.fjuanias.periferiait.authservice.application.port.out.TokenGeneratorPort;
import com.fjuanias.periferiait.authservice.domain.model.User;
import io.jsonwebtoken.Jwts;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Date;
import javax.crypto.SecretKey;
import io.jsonwebtoken.security.Keys;
import org.springframework.stereotype.Component;

/** Adaptador de salida que genera tokens JWT (HS) con jjwt. */
@Component
public class JwtTokenAdapter implements TokenGeneratorPort {

  private final JwtProperties properties;
  private final SecretKey key;

  public JwtTokenAdapter(JwtProperties properties) {
    this.properties = properties;
    this.key = Keys.hmacShaKeyFor(properties.secret().getBytes(StandardCharsets.UTF_8));
  }

  @Override
  public GeneratedToken generateFor(User user) {
    Instant now = Instant.now();
    Instant expiry = now.plusSeconds(properties.expirationSeconds());

    String token = Jwts.builder()
        .subject(user.id().toString())
        .issuer(properties.issuer())
        .claim("username", user.username())
        .claim("alias", user.alias())
        .issuedAt(Date.from(now))
        .expiration(Date.from(expiry))
        .signWith(key)
        .compact();

    return new GeneratedToken(token, properties.expirationSeconds());
  }
}
