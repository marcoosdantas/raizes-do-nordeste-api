package com.raizesdonordeste.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;

public record FidelidadeResgatarRequest(
    @Min(value = 1, message = "pontos deve ser maior que zero")
    @Schema(example = "100")
    int pontos
) {
}
