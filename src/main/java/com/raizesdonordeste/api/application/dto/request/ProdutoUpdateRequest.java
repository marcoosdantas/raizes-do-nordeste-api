package com.raizesdonordeste.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProdutoUpdateRequest(
    @Schema(example = "Tapioca de Frango com Queijo")
    String nome,
    @Schema(example = "Tapioca recheada com frango temperado e queijo coalho")
    String descricao,
    @Schema(example = "TAPIOCA")
    String categoria,
    @Schema(example = "15.90")
    BigDecimal preco,
    @Schema(example = "true")
    Boolean disponivel,
    @Schema(example = "false")
    Boolean sazonal,
    @Schema(example = "JUNINO")
    String periodoDisponivel
) {
}
