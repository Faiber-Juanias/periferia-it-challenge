package com.fjuanias.periferiait.authservice.infrastructure.adapter.out.persistence;

import com.fjuanias.periferiait.authservice.domain.model.User;

/** Traduce entre la entidad JPA (infraestructura) y el modelo de dominio. */
final class UserPersistenceMapper {

  private UserPersistenceMapper() {
  }

  static User toDomain(UserJpaEntity entity) {
    return new User(
        entity.getId(),
        entity.getUsername(),
        entity.getPasswordHash(),
        entity.getFirstName(),
        entity.getLastName(),
        entity.getBirthDate(),
        entity.getAlias(),
        entity.getCreatedAt()
    );
  }
}
