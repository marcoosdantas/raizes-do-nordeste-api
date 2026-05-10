package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.application.dto.request.PagamentoMockCallbackRequest;
import com.raizesdonordeste.api.application.dto.request.PagamentoSolicitarRequest;
import com.raizesdonordeste.api.application.dto.response.PagamentoResponse;
import com.raizesdonordeste.api.application.dto.response.PagamentoSolicitacaoResponse;
import com.raizesdonordeste.api.domain.enums.StatusPagamento;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import com.raizesdonordeste.api.domain.exception.EstoqueInsuficienteException;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.model.EstoqueItem;
import com.raizesdonordeste.api.domain.model.ItemPedido;
import com.raizesdonordeste.api.domain.model.Pagamento;
import com.raizesdonordeste.api.domain.model.Pedido;
import com.raizesdonordeste.api.domain.model.PontosCliente;
import com.raizesdonordeste.api.infrastructure.audit.AuditoriaService;
import com.raizesdonordeste.api.infrastructure.payment.PaymentMockService;
import com.raizesdonordeste.api.infrastructure.repository.EstoqueItemRepository;
import com.raizesdonordeste.api.infrastructure.repository.PagamentoRepository;
import com.raizesdonordeste.api.infrastructure.repository.PedidoRepository;
import com.raizesdonordeste.api.infrastructure.repository.PontosClienteRepository;
import com.raizesdonordeste.api.infrastructure.security.CustomUserDetails;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PagamentoService {

  private final PagamentoRepository pagamentoRepository;
  private final PedidoRepository pedidoRepository;
  private final EstoqueItemRepository estoqueItemRepository;
  private final PontosClienteRepository pontosClienteRepository;
  private final PaymentMockService paymentMockService;
  private final AuditoriaService auditoriaService;

  public PagamentoSolicitacaoResponse solicitar(PagamentoSolicitarRequest request, CustomUserDetails usuarioLogado) {
    Pedido pedido = pedidoRepository.findById(request.pedidoId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o id informado."));

    if (!pedido.getClienteId().equals(usuarioLogado.getUsuarioId())) {
      throw new AccessDeniedException("Você não possui permissão para solicitar pagamento deste pedido.");
    }

    if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
      throw new IllegalArgumentException("Somente pedidos aguardando pagamento podem ser processados.");
    }

    Pagamento pagamento = paymentMockService.criarPagamentoPendente(pedido);
    Pagamento salvo = pagamentoRepository.save(pagamento);

    auditoriaService.registrarAcao(
        "PAGAMENTO_SOLICITADO",
        "Pagamento",
        salvo.getId(),
        usuarioLogado.getUsuarioId(),
        "Pagamento pendente criado para pedido " + pedido.getId()
    );

    return new PagamentoSolicitacaoResponse(
        salvo.getId(),
        StatusPagamento.PENDENTE.name(),
        "aguardando processamento"
    );
  }

  public PagamentoResponse callback(PagamentoMockCallbackRequest request) {
    Pedido pedido = pedidoRepository.findById(request.pedidoId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o id informado."));

    Pagamento pagamento = pagamentoRepository.findTopByPedidoIdOrderByCreatedAtDesc(request.pedidoId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pagamento não encontrado para o pedido informado."));

    pagamento = paymentMockService.atualizarStatusPorCallback(pagamento, request.aprovado());
    Pagamento pagamentoSalvo = pagamentoRepository.save(pagamento);

    if (Boolean.TRUE.equals(request.aprovado())) {
      processarPagamentoAprovado(pedido);

      auditoriaService.registrarAcao(
          "PAGAMENTO_APROVADO",
          "Pagamento",
          pagamentoSalvo.getId(),
          pedido.getClienteId(),
          "Pagamento aprovado e pedido marcado como PAGO"
      );
    } else {
      auditoriaService.registrarAcao(
          "PAGAMENTO_RECUSADO",
          "Pagamento",
          pagamentoSalvo.getId(),
          pedido.getClienteId(),
          "Pagamento recusado para pedido " + pedido.getId()
      );
    }

    return toResponse(pagamentoSalvo);
  }

  private void processarPagamentoAprovado(Pedido pedido) {
    if (pedido.getStatus() != StatusPedido.AGUARDANDO_PAGAMENTO) {
      throw new IllegalArgumentException("Somente pedidos aguardando pagamento podem ser aprovados via callback.");
    }

    for (ItemPedido item : pedido.getItens()) {
      EstoqueItem estoqueItem = estoqueItemRepository
          .findByUnidadeIdAndProdutoId(pedido.getUnidadeId(), item.getProdutoId())
          .orElseThrow(() -> new EstoqueInsuficienteException("Item de estoque não encontrado para o produto " + item.getProdutoId() + "."));

      if (estoqueItem.getQuantidadeDisponivel() < item.getQuantidade()) {
        throw new EstoqueInsuficienteException("Estoque insuficiente para o produto " + item.getNomeProduto() + ".");
      }

      estoqueItem.setQuantidadeDisponivel(estoqueItem.getQuantidadeDisponivel() - item.getQuantidade());
      estoqueItem.setUpdatedAt(LocalDateTime.now());
      estoqueItemRepository.save(estoqueItem);
    }

    pedido.setStatus(StatusPedido.PAGO);
    pedido.setUpdatedAt(LocalDateTime.now());
    pedidoRepository.save(pedido);

    int pontos = calcularPontos(pedido.getTotal());
    PontosCliente pontosCliente = pontosClienteRepository.findByClienteId(pedido.getClienteId())
        .orElseGet(() -> PontosCliente.builder()
            .clienteId(pedido.getClienteId())
            .saldoPontos(0)
            .totalAcumulado(0)
            .build());

    pontosCliente.setSaldoPontos(pontosCliente.getSaldoPontos() + pontos);
    pontosCliente.setTotalAcumulado(pontosCliente.getTotalAcumulado() + pontos);
    pontosCliente.setUpdatedAt(LocalDateTime.now());
    pontosClienteRepository.save(pontosCliente);
  }

  private int calcularPontos(BigDecimal total) {
    return total.divideToIntegralValue(BigDecimal.TEN).intValue();
  }

  private PagamentoResponse toResponse(Pagamento pagamento) {
    return new PagamentoResponse(
        pagamento.getId(),
        pagamento.getPedidoId(),
        pagamento.getValorTotal(),
        pagamento.getStatus(),
        pagamento.getProvedorMock(),
        pagamento.getPayload(),
        pagamento.getCreatedAt()
    );
  }
}

