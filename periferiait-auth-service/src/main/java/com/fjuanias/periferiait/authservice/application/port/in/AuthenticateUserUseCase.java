package com.fjuanias.periferiait.authservice.application.port.in;

/** Caso de uso: autenticar a un usuario y emitir un token JWT. */
public interface AuthenticateUserUseCase {

  AuthenticationResult authenticate(AuthenticateCommand command);
  
}
