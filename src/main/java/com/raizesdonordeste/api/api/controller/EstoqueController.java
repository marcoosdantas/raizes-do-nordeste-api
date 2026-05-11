package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.api.handler.ApiErrorResponse;
import com.raizesdonordeste.api.application.dto.request.EstoqueEntradaRequest;
import com.raizesdonordeste.api.application.dto.request.EstoqueSaidaRequest;
import com.raizesdonordeste.api.application.dto.response.EstoqueItemResponse;
import com.raizesdonordeste.api.application.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/estoque")
@RequiredArgsConstructor
@Tag(name = "Estoque")
public class EstoqueController {

  private final EstoqueService estoqueService;

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
  @Operation(
      summary = "Listar estoque",
      description = "Finalidade: consultar estoque local das unidades. Auth/Permissão: JWT | Perfis: ADMIN, GERENTE."
  )
  @ApiResponse(responseCode = "200", description = "Estoque retornado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<List<EstoqueItemResponse>> listar(
      @Parameter(description = "Filtra estoque por unidade", example = "665f1f8a2c1b2a0012ab1001")
      @RequestParam(required = false) String unidadeId) {
    return ResponseEntity.ok(estoqueService.listar(unidadeId));
  }

  @PostMapping("/entrada")
  @PreAuthorize("hasRole('GERENTE')")
  @Operation(
      summary = "Registrar entrada de estoque",
      description = "Finalidade: aumentar quantidade disponível de um produto em uma unidade. Auth/Permissão: JWT | Perfis: GERENTE."
  )
  @ApiResponse(responseCode = "200", description = "Entrada registrada com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Unidade ou produto não encontrado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<EstoqueItemResponse> entrada(@Valid @RequestBody EstoqueEntradaRequest request) {
    return ResponseEntity.ok(estoqueService.entrada(request));
  }

  @PostMapping("/saida")
  @PreAuthorize("hasRole('GERENTE')")
  @Operation(
      summary = "Registrar saída de estoque",
      description = "Finalidade: reduzir quantidade disponível de um produto em uma unidade. Auth/Permissão: JWT | Perfis: GERENTE."
  )
  @ApiResponse(responseCode = "200", description = "Saída registrada com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Unidade ou produto não encontrado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", description = "Estoque insuficiente",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<EstoqueItemResponse> saida(@Valid @RequestBody EstoqueSaidaRequest request) {
    return ResponseEntity.ok(estoqueService.saida(request));
  }
}
