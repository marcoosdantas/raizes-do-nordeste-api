package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;
import io.swagger.v3.oas.annotations.media.Schema;

public record UnidadeUpdateRequest(
    @Schema(example = "Raízes Recife Centro")
    String nome,
    @Schema(example = "Recife")
    String cidade,
    @Schema(example = "PE")
    String estado,
    @Schema(example = "true")
    Boolean ativa,
    @Schema(example = "COMPLETO")
    FormatoOperacao formato,
    @Schema(example = "08:00")
    String horarioAbertura,
    @Schema(example = "22:00")
    String horarioFechamento
) {
}
