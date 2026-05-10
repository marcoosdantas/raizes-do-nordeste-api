package com.raizesdonordeste.api.application.dto.response;

import java.time.LocalDateTime;

public record HistoricoAuditoriaResponse(
    String id,
    String acao,
    String entidade,
    String entidadeId,
    String usuarioId,
    String detalhe,
    LocalDateTime timestamp
) {
}

