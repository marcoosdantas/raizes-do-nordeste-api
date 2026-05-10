package com.raizesdonordeste.api.application.dto.request;

import jakarta.validation.constraints.Min;

public record FidelidadeResgatarRequest(
    @Min(value = 1, message = "pontos deve ser maior que zero")
    int pontos
) {
}

