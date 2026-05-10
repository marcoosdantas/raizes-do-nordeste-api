package com.raizesdonordeste.api.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record PedidoItemRequest(
    @NotBlank(message = "produtoId é obrigatório")
    String produtoId,
    @Min(value = 1, message = "quantidade deve ser maior que zero")
    int quantidade
) {
}

