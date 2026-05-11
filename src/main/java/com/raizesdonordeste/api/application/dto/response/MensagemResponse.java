package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record MensagemResponse(
    @Schema(example = "Operação realizada com sucesso")
    String mensagem
) {
}
