package com.fjuanias.periferiait.authservice.application.service;

import com.fjuanias.periferiait.authservice.application.port.in.GetProfileUseCase;
import com.fjuanias.periferiait.authservice.application.port.out.LoadUserPort;
import com.fjuanias.periferiait.authservice.domain.exception.UserNotFoundException;
import com.fjuanias.periferiait.authservice.domain.model.User;
import java.util.UUID;
import org.springframework.stereotype.Service;

/** Caso de uso: recuperar el perfil del usuario autenticado. */
@Service
public class ProfileService implements GetProfileUseCase {

  private final LoadUserPort loadUserPort;

  public ProfileService(LoadUserPort loadUserPort) {
    this.loadUserPort = loadUserPort;
  }

  @Override
  public User getProfile(UUID userId) {
    return loadUserPort.loadById(userId)
        .orElseThrow(() -> new UserNotFoundException(userId));
  }
}
