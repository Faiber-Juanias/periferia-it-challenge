package com.fjuanias.periferiait.authservice.application.port.in;

import com.fjuanias.periferiait.authservice.domain.model.User;
import java.util.UUID;

/** Caso de uso: obtener el perfil del usuario autenticado. */
public interface GetProfileUseCase {

  User getProfile(UUID userId);
}
