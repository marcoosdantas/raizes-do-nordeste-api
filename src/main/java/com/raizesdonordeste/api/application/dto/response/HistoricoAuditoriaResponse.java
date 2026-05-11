package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.LocalDateTime;

public record HistoricoAuditoriaResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab7001")
    String id,
    @Schema(example = "RESGATE_PONTOS")
    String acao,
    @Schema(example = "PontosCliente")
    String entidade,
    @Schema(example = "665f1f8a2c1b2a0012ab8001")
    String entidadeId,
    @Schema(example = "665f1f8a2c1b2a0012ab5001")
    String usuarioId,
    @Schema(example = "Resgate de 100 pontos realizado")
    String detalhe,
    @Schema(example = "2026-02-05T12:00:00")
    LocalDateTime timestamp
) {
}
