package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.application.dto.request.PedidoCreateRequest;
import com.raizesdonordeste.api.application.dto.request.PedidoStatusUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.MensagemResponse;
import com.raizesdonordeste.api.application.dto.response.PagedResponse;
import com.raizesdonordeste.api.application.dto.response.PedidoResponse;
import com.raizesdonordeste.api.application.service.PedidoService;
import com.raizesdonordeste.api.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pedidos")
@RequiredArgsConstructor
@Tag(name = "Pedidos")
public class PedidoController {

  private final PedidoService pedidoService;

  @PostMapping
  @PreAuthorize("hasRole('CLIENTE')")
  @Operation(summary = "Criar pedido")
  @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
  @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
  @ApiResponse(responseCode = "422", description = "Dados inválidos")
  public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoCreateRequest request,
                                              Authentication authentication) {
    PedidoResponse response = pedidoService.criar(request, (CustomUserDetails) authentication.getPrincipal());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
  @Operation(summary = "Listar pedidos")
  @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso")
  public ResponseEntity<PagedResponse<PedidoResponse>> listar(
      @RequestParam(required = false) String canalPedido,
      @RequestParam(required = false) String status,
      @RequestParam(required = false) String unidadeId,
      @RequestParam(defaultValue = "0") int page,
      @RequestParam(defaultValue = "10") int limit
  ) {
    return ResponseEntity.ok(pedidoService.listar(canalPedido, status, unidadeId, page, limit));
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Operation(summary = "Buscar pedido por id")
  @ApiResponse(responseCode = "200", description = "Pedido retornado com sucesso")
  @ApiResponse(responseCode = "403", description = "Acesso negado")
  @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
  public ResponseEntity<PedidoResponse> buscarPorId(@PathVariable String id, Authentication authentication) {
    return ResponseEntity.ok(pedidoService.buscarPorIdComAcesso(id, (CustomUserDetails) authentication.getPrincipal()));
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('COZINHA', 'GERENTE', 'ADMIN', 'ATENDENTE')")
  @Operation(summary = "Atualizar status do pedido")
  @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
  @ApiResponse(responseCode = "409", description = "Transição inválida")
  public ResponseEntity<PedidoResponse> atualizarStatus(@PathVariable String id,
                                                        @Valid @RequestBody PedidoStatusUpdateRequest request,
                                                        Authentication authentication) {
    return ResponseEntity.ok(pedidoService.atualizarStatus(id, request, (CustomUserDetails) authentication.getPrincipal()));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
  @Operation(summary = "Cancelar pedido")
  @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso")
  public ResponseEntity<MensagemResponse> cancelar(@PathVariable String id, Authentication authentication) {
    return ResponseEntity.ok(pedidoService.cancelar(id, (CustomUserDetails) authentication.getPrincipal()));
  }
}
