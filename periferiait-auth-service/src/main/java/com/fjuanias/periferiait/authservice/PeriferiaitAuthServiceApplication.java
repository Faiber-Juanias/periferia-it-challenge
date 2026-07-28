package com.fjuanias.periferiait.authservice;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@OpenAPIDefinition(
    info = @Info(
        title = "PeriferiaIT - Auth Service API",
        version = "1.0.0",
        description = "Autenticación con JWT (HS256) y perfil del usuario."),
    security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
public class PeriferiaitAuthServiceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(PeriferiaitAuthServiceApplication.class, args);
  }
  
}
