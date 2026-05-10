package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.application.dto.request.ProdutoCreateRequest;
import com.raizesdonordeste.api.application.dto.request.ProdutoUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.PagedResponse;
import com.raizesdonordeste.api.application.dto.response.ProdutoResponse;
import com.raizesdonordeste.api.application.service.ProdutoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
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
  @Operation(summary = "Listar produtos")
  @ApiResponse(responseCode = "200", description = "Produtos retornados com sucesso")
  public ResponseEntity<PagedResponse<ProdutoResponse>> listar(
      @RequestParam(required = false) String categoria,
      @RequestParam(required = false) String unidadeId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int limit
  ) {
    return ResponseEntity.ok(produtoService.listarPublico(categoria, unidadeId, page, limit));
  }

  @GetMapping("/{id}")
  @Operation(summary = "Buscar produto por id")
  @ApiResponse(responseCode = "200", description = "Produto retornado com sucesso")
  @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  public ResponseEntity<ProdutoResponse> buscarPorId(@PathVariable String id) {
    return ResponseEntity.ok(produtoService.buscarPorId(id));
  }

  @PostMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
  @Operation(summary = "Criar produto")
  @ApiResponse(responseCode = "201", description = "Produto criado com sucesso")
  @ApiResponse(responseCode = "403", description = "Acesso negado")
  public ResponseEntity<ProdutoResponse> criar(@Valid @RequestBody ProdutoCreateRequest request) {
    ProdutoResponse response = produtoService.criar(request);
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @PutMapping("/{id}")
  @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
  @Operation(summary = "Atualizar produto")
  @ApiResponse(responseCode = "200", description = "Produto atualizado com sucesso")
  @ApiResponse(responseCode = "404", description = "Produto não encontrado")
  @ApiResponse(responseCode = "403", description = "Acesso negado")
  public ResponseEntity<ProdutoResponse> atualizar(@PathVariable String id,
                                                   @RequestBody ProdutoUpdateRequest request) {
    return ResponseEntity.ok(produtoService.atualizar(id, request));
  }
}

