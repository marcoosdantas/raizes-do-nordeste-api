package com.raizesdonordeste.api.application.dto.response;

import com.raizesdonordeste.api.domain.enums.StatusPagamento;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

public record PagamentoResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab6001")
    String id,
    @Schema(example = "665f1f8a2c1b2a0012ab3001")
    String pedidoId,
    @Schema(example = "29.80")
    BigDecimal valorTotal,
    @Schema(example = "APROVADO")
    StatusPagamento status,
    @Schema(example = "RAIZES_GATEWAY_MOCK")
    String provedorMock,
    @Schema(description = "Payload original ou metadados do gateway mock")
    Map<String, Object> payload,
    @Schema(example = "2026-02-05T12:00:00")
    LocalDateTime createdAt
) {
}
