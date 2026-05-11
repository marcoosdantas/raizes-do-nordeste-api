package com.raizesdonordeste.api.api.handler;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Detalhe de validação ou regra de negócio violada")
public record ApiErrorDetail(
    @Schema(example = "canalPedido")
    String field,
    @Schema(example = "canalPedido é obrigatório")
    String issue
) {
}
