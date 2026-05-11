package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

public record FidelidadeHistoricoResponse(
    @ArraySchema(schema = @Schema(implementation = HistoricoAuditoriaResponse.class))
    List<HistoricoAuditoriaResponse> itens
) {
}
