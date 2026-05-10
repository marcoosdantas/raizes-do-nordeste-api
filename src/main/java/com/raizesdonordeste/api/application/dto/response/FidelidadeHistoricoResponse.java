package com.raizesdonordeste.api.application.dto.response;

import java.util.List;

public record FidelidadeHistoricoResponse(
    List<HistoricoAuditoriaResponse> itens
) {
}

