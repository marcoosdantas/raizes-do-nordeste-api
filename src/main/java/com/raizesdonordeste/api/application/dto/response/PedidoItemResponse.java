package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record PedidoItemResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab2001")
    String produtoId,
    @Schema(example = "Tapioca de Frango")
    String nomeProduto,
    @Schema(example = "2")
    int quantidade,
    @Schema(example = "14.90")
    BigDecimal precoUnitario,
    @Schema(example = "29.80")
    BigDecimal subtotal
) {
}
