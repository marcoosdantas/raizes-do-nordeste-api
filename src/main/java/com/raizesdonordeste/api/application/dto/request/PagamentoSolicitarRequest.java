package com.raizesdonordeste.api.application.dto.request;

import jakarta.validation.constraints.NotBlank;

public record PagamentoSolicitarRequest(
    @NotBlank(message = "pedidoId é obrigatório")
    String pedidoId
) {
}

