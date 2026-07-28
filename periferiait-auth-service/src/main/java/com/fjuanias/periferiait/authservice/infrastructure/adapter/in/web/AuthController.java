package com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web;

import com.fjuanias.periferiait.authservice.application.port.in.AuthenticateCommand;
import com.fjuanias.periferiait.authservice.application.port.in.AuthenticateUserUseCase;
import com.fjuanias.periferiait.authservice.application.port.in.AuthenticationResult;
import com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web.dto.LoginRequest;
import com.fjuanias.periferiait.authservice.infrastructure.adapter.in.web.dto.LoginResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/** Adaptador de entrada REST para autenticación. */
@RestController
@RequestMapping("/auth")
public class AuthController {

  private final AuthenticateUserUseCase authenticateUserUseCase;

  public AuthController(AuthenticateUserUseCase authenticateUserUseCase) {
    this.authenticateUserUseCase = authenticateUserUseCase;
  }

  // Endpoint público: no requiere el bearer global (security = {}).
  @Operation(summary = "Login — valida credenciales y emite un JWT", security = {})
  @PostMapping("/login")
  public ResponseEntity<LoginResponse> login(@Valid @RequestBody LoginRequest request) {
    AuthenticationResult result = authenticateUserUseCase.authenticate(
        new AuthenticateCommand(request.username(), request.password()));
    return ResponseEntity.ok(LoginResponse.from(result));
  }
}
