package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.api.handler.ApiErrorResponse;
import com.raizesdonordeste.api.application.dto.request.FidelidadeResgatarRequest;
import com.raizesdonordeste.api.application.dto.response.FidelidadeHistoricoResponse;
import com.raizesdonordeste.api.application.dto.response.FidelidadeSaldoResponse;
import com.raizesdonordeste.api.application.dto.response.MensagemResponse;
import com.raizesdonordeste.api.application.service.FidelidadeService;
import com.raizesdonordeste.api.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/fidelidade")
@RequiredArgsConstructor
@Tag(name = "Fidelidade")
public class FidelidadeController {

  private final FidelidadeService fidelidadeService;

  @GetMapping("/saldo")
  @PreAuthorize("hasRole('CLIENTE')")
  @Operation(
      summary = "Consultar saldo de pontos",
      description = "Finalidade: consultar saldo e total acumulado no programa de fidelidade. Auth/Permissão: JWT | Perfis: CLIENTE."
  )
  @ApiResponse(responseCode = "200", description = "Saldo retornado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Consentimento LGPD ausente ou acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<FidelidadeSaldoResponse> saldo(Authentication authentication) {
    return ResponseEntity.ok(fidelidadeService.consultarSaldo((CustomUserDetails) authentication.getPrincipal()));
  }

  @GetMapping("/historico")
  @PreAuthorize("hasRole('CLIENTE')")
  @Operation(
      summary = "Consultar histórico de fidelidade",
      description = "Finalidade: consultar registros de auditoria relacionados ao programa de fidelidade. Auth/Permissão: JWT | Perfis: CLIENTE."
  )
  @ApiResponse(responseCode = "200", description = "Histórico retornado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Consentimento LGPD ausente ou acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<FidelidadeHistoricoResponse> historico(Authentication authentication) {
    return ResponseEntity.ok(fidelidadeService.consultarHistorico((CustomUserDetails) authentication.getPrincipal()));
  }

  @PostMapping("/resgatar")
  @PreAuthorize("hasRole('CLIENTE')")
  @Operation(
      summary = "Resgatar pontos",
      description = "Finalidade: converter pontos em desconto. Regra: 100 pontos equivalem a R$ 5,00. Auth/Permissão: JWT | Perfis: CLIENTE."
  )
  @ApiResponse(responseCode = "200", description = "Resgate realizado com sucesso")
  @ApiResponse(responseCode = "400", description = "Pontos inválidos ou insuficientes",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Consentimento LGPD ausente ou acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<MensagemResponse> resgatar(@Valid @RequestBody FidelidadeResgatarRequest request,
                                                   Authentication authentication) {
    return ResponseEntity.ok(fidelidadeService.resgatar(request, (CustomUserDetails) authentication.getPrincipal()));
  }
}
