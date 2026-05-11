package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.api.handler.ApiErrorResponse;
import com.raizesdonordeste.api.application.dto.request.ProdutoCreateRequest;
import com.raizesdonordeste.api.application.dto.request.ProdutoUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.PagedResponse;
import com.raizesdonordeste.api.application.dto.response.ProdutoResponse;
import com.raizesdonordeste.api.application.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/produtos")
@RequiredArgsConstructor
@Tag(name = "Produtos")
public class ProdutoController {

  private final ProdutoService produtoService;

  @GetMapping
  @SecurityRequirements
  @Operation(
      summary = "Listar produtos",
      description = "Finalidade: consultar catálogo público com filtros por categoria e disponibilidade na unidade. Auth/Permissão: público."
  )
  @ApiResponse(responseCode = "200", description = "Produtos retornados com sucesso")
  public ResponseEntity<PagedResponse<ProdutoResponse>> listar(
      @Parameter(description = "Categoria do produto", example = "BEBIDA")
      @RequestParam(required = false) String categoria,
      @Parameter(description = "Filtra produtos disponíveis no estoque da unidade", example = "665f1f8a2c1b2a0012ab1001")
      @RequestParam(required = false) String unidadeId,
      @Parameter(description = "Página inicial em zero", example = "0")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Quantidade de itens por página", example = "10")
      @RequestParam(defaultValue = "10") int limit
  ) {
    return ResponseEntity.ok(produtoService.listarPublico(categoria, unidadeId, page, limit));
  }

  @GetMapping("/{id}")
  @SecurityRequirements
  @Operation(
      summary = "Buscar produto por id",
      description = "Finalidade: consultar detalhes de um produto do catálogo. Auth/Permissão: público."
  )
  @ApiResponse(responseCode = "200", description = "Produto retornado com sucesso")
  @ApiResponse(responseCode = "404", description = "Produto não encontrado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<ProdutoResponse> buscarPorId(
      @Parameter(description = "Identificador do produto", example = "665f1f8a2c1b2a0012ab2001")
      @PathVariable String id) {
    return ResponseEntity.ok(produtoService.buscarPorId(id));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
  @Operation(
      summary = "Criar produto",
      description = "Finalidade: cadastrar item no catálogo. Auth/Permissão: JWT | Perfis: ADMIN, GERENTE."
  )
  @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoCreateRequest request) {
    ProdutoResponse response = produtoService.criar(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
  @Operation(
      summary = "Atualizar produto",
      description = "Finalidade: atualizar dados de um produto existente. Auth/Permissão: JWT | Perfis: ADMIN, GERENTE."
  )
  @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Produto não encontrado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<ProdutoResponse> atualizar(
                                                   @Parameter(description = "Identificador do produto", example = "665f1f8a2c1b2a0012ab2001")
                                                   @PathVariable String id,
                                                   @RequestBody ProdutoUpdateRequest request) {
    return ResponseEntity.ok(produtoService.atualizar(id, request));
  }
}
