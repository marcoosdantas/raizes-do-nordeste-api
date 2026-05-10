package com.raizesdonordeste.api.application.dto.request;

import java.math.BigDecimal;

public record ProdutoUpdateRequest(
    String nome,
    String descricao,
    String categoria,
    BigDecimal preco,
    Boolean disponivel,
    Boolean sazonal,
    String periodoDisponivel
) {
}

