package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.api.handler.ApiErrorResponse;
import com.raizesdonordeste.api.application.dto.request.PedidoCreateRequest;
import com.raizesdonordeste.api.application.dto.request.PedidoStatusUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.MensagemResponse;
import com.raizesdonordeste.api.application.dto.response.PagedResponse;
import com.raizesdonordeste.api.application.dto.response.PedidoResponse;
import com.raizesdonordeste.api.application.service.PedidoService;
import com.raizesdonordeste.api.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
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
  @Operation(
      summary = "Criar pedido",
      description = "Finalidade: criar pedido validando unidade ativa, canal, produtos e estoque. O estoque não é deduzido na criação. Auth/Permissão: JWT | Perfis: CLIENTE."
  )
  @ApiResponse(responseCode = "201", description = "Pedido criado com sucesso")
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
  public ResponseEntity<PedidoResponse> criar(@Valid @RequestBody PedidoCreateRequest request,
                                              Authentication authentication) {
    PedidoResponse response = pedidoService.criar(request, (CustomUserDetails) authentication.getPrincipal());
    return ResponseEntity.status(HttpStatus.CREATED).body(response);
  }

  @GetMapping
  @PreAuthorize("hasAnyRole('ADMIN', 'GERENTE')")
  @Operation(
      summary = "Listar pedidos",
      description = "Finalidade: listar pedidos com filtros por canal, status e unidade. Auth/Permissão: JWT | Perfis: ADMIN, GERENTE."
  )
  @ApiResponse(responseCode = "200", description = "Pedidos retornados com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<PagedResponse<PedidoResponse>> listar(
      @Parameter(description = "Canal do pedido", example = "APP")
      @RequestParam(required = false) String canalPedido,
      @Parameter(description = "Status do pedido", example = "PAGO")
      @RequestParam(required = false) String status,
      @Parameter(description = "Identificador da unidade", example = "665f1f8a2c1b2a0012ab1001")
      @RequestParam(required = false) String unidadeId,
      @Parameter(description = "Página inicial em zero", example = "0")
      @RequestParam(defaultValue = "0") int page,
      @Parameter(description = "Quantidade de itens por página", example = "10")
      @RequestParam(defaultValue = "10") int limit
  ) {
    return ResponseEntity.ok(pedidoService.listar(canalPedido, status, unidadeId, page, limit));
  }

  @GetMapping("/{id}")
  @PreAuthorize("isAuthenticated()")
  @Operation(
      summary = "Buscar pedido por id",
      description = "Finalidade: consultar um pedido. Auth/Permissão: JWT | Perfis: dono do pedido, ADMIN ou GERENTE."
  )
  @ApiResponse(responseCode = "200", description = "Pedido retornado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<PedidoResponse> buscarPorId(
      @Parameter(description = "Identificador do pedido", example = "665f1f8a2c1b2a0012ab3001")
      @PathVariable String id, Authentication authentication) {
    return ResponseEntity.ok(pedidoService.buscarPorIdComAcesso(id, (CustomUserDetails) authentication.getPrincipal()));
  }

  @PatchMapping("/{id}/status")
  @PreAuthorize("hasAnyRole('COZINHA', 'GERENTE', 'ADMIN', 'ATENDENTE')")
  @Operation(
      summary = "Atualizar status do pedido",
      description = "Finalidade: avançar o status do pedido respeitando transições permitidas. Auth/Permissão: JWT | Perfis: COZINHA, ATENDENTE, GERENTE, ADMIN."
  )
  @ApiResponse(responseCode = "200", description = "Status atualizado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "409", description = "Transição inválida",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "422", description = "Dados inválidos",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<PedidoResponse> atualizarStatus(
                                                        @Parameter(description = "Identificador do pedido", example = "665f1f8a2c1b2a0012ab3001")
                                                        @PathVariable String id,
                                                        @Valid @RequestBody PedidoStatusUpdateRequest request,
                                                        Authentication authentication) {
    return ResponseEntity.ok(pedidoService.atualizarStatus(id, request, (CustomUserDetails) authentication.getPrincipal()));
  }

  @DeleteMapping("/{id}")
  @PreAuthorize("hasAnyRole('GERENTE', 'ADMIN')")
  @Operation(
      summary = "Cancelar pedido",
      description = "Finalidade: cancelar pedido e registrar auditoria. Auth/Permissão: JWT | Perfis: GERENTE, ADMIN."
  )
  @ApiResponse(responseCode = "200", description = "Pedido cancelado com sucesso")
  @ApiResponse(responseCode = "401", description = "Token ausente ou inválido",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "403", description = "Acesso negado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  @ApiResponse(responseCode = "404", description = "Pedido não encontrado",
      content = @Content(schema = @Schema(implementation = ApiErrorResponse.class)))
  public ResponseEntity<MensagemResponse> cancelar(
      @Parameter(description = "Identificador do pedido", example = "665f1f8a2c1b2a0012ab3001")
      @PathVariable String id, Authentication authentication) {
    return ResponseEntity.ok(pedidoService.cancelar(id, (CustomUserDetails) authentication.getPrincipal()));
  }
}
