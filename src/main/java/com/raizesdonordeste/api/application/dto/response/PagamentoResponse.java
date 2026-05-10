package com.raizesdonordeste.api.application.dto.response;

import com.raizesdonordeste.api.domain.enums.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record PagamentoResponse(
    String id,
    String pedidoId,
    BigDecimal valorTotal,
    StatusPagamento status,
    String provedorMock,
    Map<String, Object> payload,
    LocalDateTime createdAt
) {
}

