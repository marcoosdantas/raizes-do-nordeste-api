package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record PagamentoSolicitacaoResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab6001")
    String pagamentoId,
    @Schema(example = "PENDENTE")
    String status,
    @Schema(example = "aguardando processamento")
    String mensagem
) {
}
