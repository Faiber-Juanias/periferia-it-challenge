package com.fjuanias.periferiait.logicservice.application.port.out;

import java.util.UUID;

/** Puerto de salida para dar/quitar like (vía procedure sp_toggle_like). */
public interface ToggleLikePort {

  /** Ejecuta el toggle y devuelve el total de likes actualizado. */
  long toggle(UUID postId, UUID userId);
}
