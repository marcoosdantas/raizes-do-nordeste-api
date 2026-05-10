package com.raizesdonordeste.api.api.controller;

import com.raizesdonordeste.api.application.dto.request.PagamentoMockCallbackRequest;
import com.raizesdonordeste.api.application.dto.request.PagamentoSolicitarRequest;
import com.raizesdonordeste.api.application.dto.response.PagamentoResponse;
import com.raizesdonordeste.api.application.dto.response.PagamentoSolicitacaoResponse;
import com.raizesdonordeste.api.application.service.PagamentoService;
import com.raizesdonordeste.api.infrastructure.security.CustomUserDetails;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pagamentos")
@RequiredArgsConstructor
@Tag(name = "Pagamentos")
public class PagamentoController {

  private final PagamentoService pagamentoService;

  @PostMapping("/solicitar")
  @PreAuthorize("hasRole('CLIENTE')")
  @Operation(summary = "Solicitar pagamento do pedido")
  @ApiResponse(responseCode = "200", description = "Pagamento solicitado com sucesso")
  @ApiResponse(responseCode = "403", description = "Acesso negado")
  @ApiResponse(responseCode = "404", description = "Pedido não encontrado")
  public ResponseEntity<PagamentoSolicitacaoResponse> solicitar(@Valid @RequestBody PagamentoSolicitarRequest request,
                                                                Authentication authentication) {
    return ResponseEntity.ok(pagamentoService.solicitar(request, (CustomUserDetails) authentication.getPrincipal()));
  }

  @PostMapping("/mock/callback")
  @Operation(summary = "Callback mock do gateway de pagamento")
  @ApiResponse(responseCode = "200", description = "Callback processado com sucesso")
  @ApiResponse(responseCode = "404", description = "Pedido ou pagamento não encontrado")
  @ApiResponse(responseCode = "409", description = "Estoque insuficiente")
  public ResponseEntity<PagamentoResponse> callback(@Valid @RequestBody PagamentoMockCallbackRequest request) {
    return ResponseEntity.ok(pagamentoService.callback(request));
  }
}

