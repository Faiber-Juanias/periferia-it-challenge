package com.fjuanias.periferiait.authservice.infrastructure.adapter.out.persistence;

import com.fjuanias.periferiait.authservice.application.port.out.LoadUserPort;
import com.fjuanias.periferiait.authservice.domain.model.User;
import java.util.Optional;
import java.util.UUID;
import org.springframework.stereotype.Component;

/** Adaptador de salida que implementa {@link LoadUserPort} sobre JPA. */
@Component
public class UserPersistenceAdapter implements LoadUserPort {

  private final UserJpaRepository repository;

  public UserPersistenceAdapter(UserJpaRepository repository) {
    this.repository = repository;
  }

  @Override
  public Optional<User> loadByUsername(String username) {
    return repository.findByUsername(username).map(UserPersistenceMapper::toDomain);
  }

  @Override
  public Optional<User> loadById(UUID userId) {
    return repository.findById(userId).map(UserPersistenceMapper::toDomain);
  }
}
