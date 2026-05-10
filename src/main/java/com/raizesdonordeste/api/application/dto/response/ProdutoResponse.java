package com.raizesdonordeste.api.application.dto.response;

import java.math.BigDecimal;

public record ProdutoResponse(
    String id,
    String nome,
    String descricao,
    String categoria,
    BigDecimal preco,
    boolean disponivel,
    boolean sazonal,
    String periodoDisponivel
) {
}

