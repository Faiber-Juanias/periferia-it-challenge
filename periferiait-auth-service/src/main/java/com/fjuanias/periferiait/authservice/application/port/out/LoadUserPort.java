package com.fjuanias.periferiait.authservice.application.port.out;

import com.fjuanias.periferiait.authservice.domain.model.User;
import java.util.Optional;
import java.util.UUID;

/** Puerto de salida para recuperar usuarios desde el almacenamiento. */
public interface LoadUserPort {

  Optional<User> loadByUsername(String username);

  Optional<User> loadById(UUID userId);

}
