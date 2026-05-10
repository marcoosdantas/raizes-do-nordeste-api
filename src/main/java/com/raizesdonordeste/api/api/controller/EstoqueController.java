package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.application.dto.request.EstoqueEntradaRequest;
import com.raizesdonordeste.api.application.dto.request.EstoqueSaidaRequest;
import com.raizesdonordeste.api.application.dto.response.EstoqueItemResponse;
import com.raizesdonordeste.api.application.service.EstoqueService;
import io.swagger.v3.oas.annotations.Operation;
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
  @Operation(summary = "Listar estoque")
  @ApiResponse(responseCode = "200", description = "Estoque retornado com sucesso")
  @ApiResponse(responseCode = "403", description = "Acesso negado")
  public ResponseEntity<List<EstoqueItemResponse>> listar(@RequestParam(required = false) String unidadeId) {
    return ResponseEntity.ok(estoqueService.listar(unidadeId));
  }

  @PostMapping("/entrada")
  @PreAuthorize("hasRole('GERENTE')")
  @Operation(summary = "Registrar entrada de estoque")
  @ApiResponse(responseCode = "200", description = "Entrada registrada com sucesso")
  @ApiResponse(responseCode = "403", description = "Acesso negado")
  @ApiResponse(responseCode = "404", description = "Unidade ou produto não encontrado")
  public ResponseEntity<EstoqueItemResponse> entrada(@Valid @RequestBody EstoqueEntradaRequest request) {
    return ResponseEntity.ok(estoqueService.entrada(request));
  }

  @PostMapping("/saida")
  @PreAuthorize("hasRole('GERENTE')")
  @Operation(summary = "Registrar saída de estoque")
  @ApiResponse(responseCode = "200", description = "Saída registrada com sucesso")
  @ApiResponse(responseCode = "403", description = "Acesso negado")
  @ApiResponse(responseCode = "404", description = "Unidade ou produto não encontrado")
  @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
  public ResponseEntity<EstoqueItemResponse> saida(@Valid @RequestBody EstoqueSaidaRequest request) {
    return ResponseEntity.ok(estoqueService.saida(request));
  }
}

