package com.raizesdonordeste.api.application.dto.response;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PedidoResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab3001")
    String id,
    @Schema(example = "665f1f8a2c1b2a0012ab5001")
    String clienteId,
    @Schema(example = "665f1f8a2c1b2a0012ab1001")
    String unidadeId,
    @Schema(example = "APP")
    CanalPedido canalPedido,
    @ArraySchema(schema = @Schema(implementation = PedidoItemResponse.class))
    List<PedidoItemResponse> itens,
    @Schema(example = "AGUARDANDO_PAGAMENTO")
    StatusPedido status,
    @Schema(example = "29.80")
    BigDecimal total,
    @Schema(example = "PIX")
    String formaPagamento,
    @Schema(example = "2026-02-05T12:00:00")
    LocalDateTime createdAt,
    @Schema(example = "2026-02-05T12:00:00")
    LocalDateTime updatedAt
) {
}
