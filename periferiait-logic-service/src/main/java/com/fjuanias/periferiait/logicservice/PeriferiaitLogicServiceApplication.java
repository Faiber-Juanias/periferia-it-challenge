package com.fjuanias.periferiait.logicservice;

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
        title = "PeriferiaIT - Logic Service API",
        version = "1.0.0",
        description = "Publicaciones y likes (difusión en tiempo real por WebSocket "
            + "STOMP en /topic/posts.likes)."),
    security = @SecurityRequirement(name = "bearerAuth"))
@SecurityScheme(
    name = "bearerAuth",
    type = SecuritySchemeType.HTTP,
    scheme = "bearer",
    bearerFormat = "JWT")
public class PeriferiaitLogicServiceApplication {
  
  public static void main(String[] args) {
    SpringApplication.run(PeriferiaitLogicServiceApplication.class, args);
  }
  
}
