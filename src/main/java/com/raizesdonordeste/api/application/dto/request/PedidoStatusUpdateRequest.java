package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.StatusPedido;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

public record PedidoStatusUpdateRequest(
    @NotNull(message = "status é obrigatório")
    @Schema(example = "EM_PREPARO")
    StatusPedido status
) {
}
