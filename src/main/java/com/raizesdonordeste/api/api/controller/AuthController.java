package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.application.dto.request.AuthLoginRequest;
import com.raizesdonordeste.api.application.dto.request.AuthRegisterRequest;
import com.raizesdonordeste.api.application.dto.response.AuthResponse;
import com.raizesdonordeste.api.application.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "Registrar cliente")
  @ApiResponse(responseCode = "201", description = "Cliente registrado com sucesso")
  @ApiResponse(responseCode = "400", description = "E-mail já cadastrado ou requisição inválida")
  @ApiResponse(responseCode = "422", description = "Dados de cadastro inválidos")
  public ResponseEntity<AuthResponse> register(@Valid @RequestBody AuthRegisterRequest request) {
    AuthResponse response = authService.register(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PostMapping("/login")
  @Operation(summary = "Autenticar usuário")
  @ApiResponse(responseCode = "200", description = "Login realizado com sucesso")
  @ApiResponse(responseCode = "400", description = "Requisição inválida")
  @ApiResponse(responseCode = "401", description = "Credenciais inválidas")
  @ApiResponse(responseCode = "422", description = "Dados de login inválidos")
  public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthLoginRequest request) {
    AuthResponse response = authService.login(request);
    return ResponseEntity.ok(response);
  }
}
