package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.api.handler.ApiErrorResponse;
import com.raizesdonordeste.api.application.dto.request.AuthLoginRequest;
import com.raizesdonordeste.api.application.dto.request.AuthRegisterRequest;
import com.raizesdonordeste.api.application.dto.response.AuthResponse;
import com.raizesdonordeste.api.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "Auth", description = "Autenticação e registro de usuários")
public class AuthController {

  private final AuthService authService;

  @PostMapping("/register")
  @SecurityRequirements
  @Operation(
      summary = "Registrar cliente",
      description = "Finalidade: criar uma conta de cliente com consentimento LGPD. Auth/Permissão: público."
  )
  @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso")
  @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou requisição inválida",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados de cadastro inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
    AuthResponse response = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  @SecurityRequirements
  @Operation(
      summary = "Autenticar usuário",
      description = "Finalidade: autenticar usuário e retornar JWT com claims de id, email e perfil. Auth/Permissão: público."
  )
  @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
  @ApiResponse(responseCode = "400", description = "Requisição inválida",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "401", description = "Credenciais inválidas",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados de login inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
    AuthResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }
}
