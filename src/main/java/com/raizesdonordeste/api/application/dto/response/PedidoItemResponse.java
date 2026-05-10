package com.raizesdonordeste.api.application.dto.response;

import java.math.BigDecimal;

public record PedidoItemResponse(
    String produtoId,
    String nomeProduto,
    int quantidade,
    BigDecimal precoUnitario,
    BigDecimal subtotal
) {
}

