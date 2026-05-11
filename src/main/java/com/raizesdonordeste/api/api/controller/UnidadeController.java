package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.api.handler.ApiErrorResponse;
import com.raizesdonordeste.api.application.dto.request.UnidadeCreateRequest;
import com.raizesdonordeste.api.application.dto.request.UnidadeUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.UnidadeResponse;
import com.raizesdonordeste.api.application.service.UnidadeService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/unidades")
@RequiredArgsConstructor
@Tag(name = "Unidades")
public class UnidadeController {

  private final UnidadeService unidadeService;

  @GetMapping
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Listar unidades",
      description = "Finalidade: consultar unidades franqueadas ativas e cadastradas. Auth/Permissão: JWT | Perfis: qualquer usuário autenticado."
  )
  @ApiResponse(responseCode = "200", description = "Unidades retornadas com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<List<UnidadeResponse>> listar() {
    return ResponseEntity.ok(unidadeService.listar());
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Buscar unidade por id",
      description = "Finalidade: consultar detalhes de uma unidade. Auth/Permissão: JWT | Perfis: qualquer usuário autenticado."
  )
  @ApiResponse(responseCode = "200", description = "Unidade retornada com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Unidade não encontrada",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<UnidadeResponse> buscarPorId(
      @Parameter(description = "Identificador da unidade", example = "665f1f8a2c1b2a0012ab1001")
      @PathVariable String id) {
    return ResponseEntity.ok(unidadeService.buscarPorId(id));
  }

  @PostMapping
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(
      summary = "Criar unidade",
      description = "Finalidade: cadastrar nova unidade franqueada. Auth/Permissão: JWT | Perfis: ADMIN."
  )
  @ApiResponse(responseCode = "201", description = "Unidade criada com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<UnidadeResponse> criar(@Valid @RequestBody UnidadeCreateRequest request) {
    UnidadeResponse response = unidadeService.criar(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PatchMapping("/{id}")
  @PreAuthorize("hasRole('ADMIN')")
  @Operation(
      summary = "Atualizar unidade parcialmente",
      description = "Finalidade: alterar dados operacionais de uma unidade. Auth/Permissão: JWT | Perfis: ADMIN."
  )
  @ApiResponse(responseCode = "200", description = "Unidade atualizada com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Unidade não encontrada",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<UnidadeResponse> atualizar(
                                                   @Parameter(description = "Identificador da unidade", example = "665f1f8a2c1b2a0012ab1001")
                                                   @PathVariable String id,
                                                   @RequestBody UnidadeUpdateRequest request) {
    return ResponseEntity.ok(unidadeService.atualizarParcial(id, request));
  }
}
