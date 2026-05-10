package com.raizesdonordeste.api.application.dto.response;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
    String id,
    String clienteId,
    String unidadeId,
    CanalPedido canalPedido,
    List<PedidoItemResponse> itens,
    StatusPedido status,
    BigDecimal total,
    String formaPagamento,
    LocalDateTime createdAt,
    LocalDateTime updatedAt
) {
}

