package com.raizesdonordeste.api.application.dto.response;

import java.time.LocalDateTime;

public record FidelidadeSaldoResponse(
    String clienteId,
    int saldoPontos,
    int totalAcumulado,
    LocalDateTime updatedAt
) {
}

