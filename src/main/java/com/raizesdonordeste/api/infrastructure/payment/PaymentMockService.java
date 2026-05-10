package com.raizesdonordeste.api.infrastructure.payment;

import com.raizesdonordeste.api.domain.enums.StatusPagamento;
import com.raizesdonordeste.api.domain.model.Pagamento;
import com.raizesdonordeste.api.domain.model.Pedido;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PaymentMockService {

  public Pagamento criarPagamentoPendente(Pedido pedido) {
    Map<String, Object> payload = new HashMap<>();
    payload.put("pedidoId", pedido.getId());
    payload.put("status", StatusPagamento.PENDENTE.name());
    payload.put("mensagem", "aguardando processamento");
    payload.put("requestedAt", LocalDateTime.now().toString());

    log.info("Mock de pagamento criado para pedido {}", pedido.getId());

    return Pagamento.builder()
        .pedidoId(pedido.getId())
        .valorTotal(pedido.getTotal())
        .status(StatusPagamento.PENDENTE)
        .provedorMock("RAIZES_GATEWAY_MOCK")
        .payload(payload)
        .createdAt(LocalDateTime.now())
        .build();
  }

  public Pagamento atualizarStatusPorCallback(Pagamento pagamento, boolean aprovado) {
    StatusPagamento novoStatus = aprovado ? StatusPagamento.APROVADO : StatusPagamento.RECUSADO;

    Map<String, Object> payload = pagamento.getPayload() == null ? new HashMap<>() : new HashMap<>(pagamento.getPayload());
    payload.put("callbackAprovado", aprovado);
    payload.put("callbackAt", LocalDateTime.now().toString());
    payload.put("status", novoStatus.name());
    pagamento.setPayload(payload);
    pagamento.setStatus(novoStatus);

    log.info("Callback de pagamento processado para pedido {} com status {}", pagamento.getPedidoId(), novoStatus);

    return pagamento;
  }
}

