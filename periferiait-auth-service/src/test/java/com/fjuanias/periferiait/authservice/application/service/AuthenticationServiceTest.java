package com.fjuanias.periferiait.authservice.application.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fjuanias.periferiait.authservice.application.port.in.AuthenticateCommand;
import com.fjuanias.periferiait.authservice.application.port.in.AuthenticationResult;
import com.fjuanias.periferiait.authservice.application.port.out.GeneratedToken;
import com.fjuanias.periferiait.authservice.application.port.out.LoadUserPort;
import com.fjuanias.periferiait.authservice.application.port.out.PasswordVerifierPort;
import com.fjuanias.periferiait.authservice.application.port.out.TokenGeneratorPort;
import com.fjuanias.periferiait.authservice.domain.exception.InvalidCredentialsException;
import com.fjuanias.periferiait.authservice.domain.model.User;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

  @Mock
  private LoadUserPort loadUserPort;
  @Mock
  private PasswordVerifierPort passwordVerifierPort;
  @Mock
  private TokenGeneratorPort tokenGeneratorPort;
  @InjectMocks
  private AuthenticationService service;

  private User sampleUser(String username, String alias) {
    return new User(UUID.randomUUID(), username, "hashed", "Juan", "Pérez",
        LocalDate.of(1990, 5, 14), alias, OffsetDateTime.now());
  }

  @Test
  void authenticate_returnsToken_whenCredentialsAreValid() {
    User user = sampleUser("jdoe", "juanp");
    when(loadUserPort.loadByUsername("jdoe")).thenReturn(Optional.of(user));
    when(passwordVerifierPort.matches("secret", "hashed")).thenReturn(true);
    when(tokenGeneratorPort.generateFor(user)).thenReturn(new GeneratedToken("jwt-token", 3600));

    AuthenticationResult result = service.authenticate(new AuthenticateCommand("jdoe", "secret"));

    assertThat(result.token()).isEqualTo("jwt-token");
    assertThat(result.alias()).isEqualTo("juanp");
    assertThat(result.expiresInSeconds()).isEqualTo(3600);
  }

  @Test
  void authenticate_throws_whenUserDoesNotExist() {
    when(loadUserPort.loadByUsername("ghost")).thenReturn(Optional.empty());

    assertThatThrownBy(() -> service.authenticate(new AuthenticateCommand("ghost", "x")))
        .isInstanceOf(InvalidCredentialsException.class);

    verify(tokenGeneratorPort, never()).generateFor(any());
  }

  @Test
  void authenticate_throws_whenPasswordIsWrong() {
    User user = sampleUser("jdoe", "juanp");
    when(loadUserPort.loadByUsername("jdoe")).thenReturn(Optional.of(user));
    when(passwordVerifierPort.matches("wrong", "hashed")).thenReturn(false);

    assertThatThrownBy(() -> service.authenticate(new AuthenticateCommand("jdoe", "wrong")))
        .isInstanceOf(InvalidCredentialsException.class);

    verify(tokenGeneratorPort, never()).generateFor(any());
  }
}
