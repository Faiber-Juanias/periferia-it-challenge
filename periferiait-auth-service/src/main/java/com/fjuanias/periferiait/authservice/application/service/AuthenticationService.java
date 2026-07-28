package com.fjuanias.periferiait.authservice.application.service;

import com.fjuanias.periferiait.authservice.application.port.in.AuthenticateCommand;
import com.fjuanias.periferiait.authservice.application.port.in.AuthenticateUserUseCase;
import com.fjuanias.periferiait.authservice.application.port.in.AuthenticationResult;
import com.fjuanias.periferiait.authservice.application.port.out.GeneratedToken;
import com.fjuanias.periferiait.authservice.application.port.out.LoadUserPort;
import com.fjuanias.periferiait.authservice.application.port.out.PasswordVerifierPort;
import com.fjuanias.periferiait.authservice.application.port.out.TokenGeneratorPort;
import com.fjuanias.periferiait.authservice.domain.exception.InvalidCredentialsException;
import com.fjuanias.periferiait.authservice.domain.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Caso de uso de autenticación: valida credenciales y emite un JWT.
 * No conoce detalles de JPA, BCrypt ni jjwt; solo habla con puertos.
 */
@Service
public class AuthenticationService implements AuthenticateUserUseCase {

  private static final Logger log = LoggerFactory.getLogger(AuthenticationService.class);

  private final LoadUserPort loadUserPort;
  private final PasswordVerifierPort passwordVerifierPort;
  private final TokenGeneratorPort tokenGeneratorPort;

  public AuthenticationService(LoadUserPort loadUserPort,
                               PasswordVerifierPort passwordVerifierPort,
                               TokenGeneratorPort tokenGeneratorPort) {
    this.loadUserPort = loadUserPort;
    this.passwordVerifierPort = passwordVerifierPort;
    this.tokenGeneratorPort = tokenGeneratorPort;
  }

  @Override
  public AuthenticationResult authenticate(AuthenticateCommand command) {
    User user = loadUserPort.loadByUsername(command.username())
        .orElseThrow(() -> {
          log.info("Login fallido: usuario '{}' no existe", command.username());
          return new InvalidCredentialsException();
        });

    if (!passwordVerifierPort.matches(command.rawPassword(), user.passwordHash())) {
      log.info("Login fallido: contraseña incorrecta para '{}'", command.username());
      throw new InvalidCredentialsException();
    }

    GeneratedToken token = tokenGeneratorPort.generateFor(user);
    log.info("Login exitoso para '{}' (id={})", user.username(), user.id());

    return new AuthenticationResult(
        token.value(),
        token.expiresInSeconds(),
        user.id(),
        user.username(),
        user.alias()
    );
  }
}
