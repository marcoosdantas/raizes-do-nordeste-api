package com.raizesdonordeste.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PagamentoSolicitarRequest(
    @NotBlank(message = "pedidoId é obrigatório")
    @Schema(example = "665f1f8a2c1b2a0012ab3001")
    String pedidoId
) {
}
