package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.StatusPedido;
import jakarta.validation.constraints.NotNull;

public record PedidoStatusUpdateRequest(
    @NotNull(message = "status é obrigatório")
    StatusPedido status
) {
}

