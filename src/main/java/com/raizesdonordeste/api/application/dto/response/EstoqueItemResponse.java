package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record EstoqueItemResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab4001")
    String id,
    @Schema(example = "665f1f8a2c1b2a0012ab1001")
    String unidadeId,
    @Schema(example = "665f1f8a2c1b2a0012ab2001")
    String produtoId,
    @Schema(example = "50")
    int quantidadeDisponivel,
    @Schema(example = "5")
    int quantidadeMinima,
    @Schema(example = "2026-02-05T12:00:00")
    LocalDateTime updatedAt
) {
}
