package com.raizesdonordeste.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record EstoqueSaidaRequest(
    @NotBlank(message = "unidadeId é obrigatório")
    @Schema(example = "665f1f8a2c1b2a0012ab1001")
    String unidadeId,
    @NotBlank(message = "produtoId é obrigatório")
    @Schema(example = "665f1f8a2c1b2a0012ab2001")
    String produtoId,
    @Min(value = 1, message = "quantidade deve ser maior que zero")
    @Schema(example = "2")
    int quantidade
) {
}
