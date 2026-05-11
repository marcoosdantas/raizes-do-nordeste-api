package com.raizesdonordeste.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record PagamentoMockCallbackRequest(
    @NotBlank(message = "pedidoId é obrigatório")
    @Schema(example = "665f1f8a2c1b2a0012ab3001")
    String pedidoId,
    @NotNull(message = "aprovado é obrigatório")
    @Schema(example = "true")
    Boolean aprovado
) {
}
