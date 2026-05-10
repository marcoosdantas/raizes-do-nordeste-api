package com.raizesdonordeste.api.application.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PagamentoMockCallbackRequest(
    @NotBlank(message = "pedidoId é obrigatório")
    String pedidoId,
    @NotNull(message = "aprovado é obrigatório")
    Boolean aprovado
) {
}

