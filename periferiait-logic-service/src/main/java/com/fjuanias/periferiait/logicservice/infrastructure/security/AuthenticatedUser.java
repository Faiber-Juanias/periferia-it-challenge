package com.fjuanias.periferiait.logicservice.infrastructure.security;

import java.util.UUID;

/** Principal autenticado extraído del JWT (id + alias del usuario). */
public record AuthenticatedUser(UUID id, String alias) {
}
