package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProdutoResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab2001")
    String id,
    @Schema(example = "Tapioca de Frango")
    String nome,
    @Schema(example = "Tapioca recheada com frango temperado e queijo coalho")
    String descricao,
    @Schema(example = "TAPIOCA")
    String categoria,
    @Schema(example = "14.90")
    BigDecimal preco,
    @Schema(example = "true")
    boolean disponivel,
    @Schema(example = "false")
    boolean sazonal,
    @Schema(example = "JUNINO")
    String periodoDisponivel
) {
}
