package com.raizesdonordeste.api.application.dto.response;

import java.time.LocalDateTime;

public record EstoqueItemResponse(
    String id,
    String unidadeId,
    String produtoId,
    int quantidadeDisponivel,
    int quantidadeMinima,
    LocalDateTime updatedAt
) {
}

