package com.fjuanias.periferiait.authservice.application.port.out;

import com.fjuanias.periferiait.authservice.domain.model.User;

/** Puerto de salida para generar un token JWT a partir de un usuario. */
public interface TokenGeneratorPort {

  GeneratedToken generateFor(User user);
}
