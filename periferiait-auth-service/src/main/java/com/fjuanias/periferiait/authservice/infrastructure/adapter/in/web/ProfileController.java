package com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web;

import com.fjuanias.periferiait.authservice.application.port.in.GetProfileUseCase;
import com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web.dto.ProfileResponse;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Adaptador de entrada REST para el perfil del usuario autenticado. */
@RestController
@RequestMapping("/users")
public class ProfileController {

  private final GetProfileUseCase getProfileUseCase;

  public ProfileController(GetProfileUseCase getProfileUseCase) {
    this.getProfileUseCase = getProfileUseCase;
  }

  @GetMapping("/me")
  public ResponseEntity<ProfileResponse> me(@AuthenticationPrincipal String userId) {
    ProfileResponse profile = ProfileResponse.from(
        getProfileUseCase.getProfile(UUID.fromString(userId)));
    return ResponseEntity.ok(profile);
  }
}
