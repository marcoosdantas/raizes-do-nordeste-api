package com.raizesdonordeste.api.application.dto.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EstoqueEntradaRequest(
    @NotBlank(message = "unidadeId é obrigatório")
    String unidadeId,
    @NotBlank(message = "produtoId é obrigatório")
    String produtoId,
    @Min(value = 1, message = "quantidade deve ser maior que zero")
    int quantidade
) {
}

