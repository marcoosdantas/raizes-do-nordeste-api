package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record FidelidadeSaldoResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab5001")
    String clienteId,
    @Schema(example = "120")
    int saldoPontos,
    @Schema(example = "320")
    int totalAcumulado,
    @Schema(example = "2026-02-05T12:00:00")
    LocalDateTime updatedAt
) {
}
