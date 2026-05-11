package com.raizesdonordeste.api.application.dto.response;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;
import io.swagger.v3.oas.annotations.media.Schema;

public record UnidadeResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab1001")
    String id,
    @Schema(example = "Raízes Recife Centro")
    String nome,
    @Schema(example = "Recife")
    String cidade,
    @Schema(example = "PE")
    String estado,
    @Schema(example = "true")
    boolean ativa,
    @Schema(example = "COMPLETO")
    FormatoOperacao formato,
    @Schema(example = "08:00")
    String horarioAbertura,
    @Schema(example = "22:00")
    String horarioFechamento
) {
}
