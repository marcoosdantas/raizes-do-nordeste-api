package com.raizesdonordeste.api.api.handler;

import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import java.util.List;

@Schema(description = "Resposta padronizada de erro da API")
public record ApiErrorResponse(
    @Schema(example = "ESTOQUE_INSUFICIENTE")
    String error,
    @Schema(example = "Estoque insuficiente para o produto informado.")
    String message,
    @ArraySchema(schema = @Schema(implementation = ApiErrorDetail.class))
    List<ApiErrorDetail> details,
    @Schema(example = "2026-02-05T12:00:00Z")
    String timestamp,
    @Schema(example = "/pedidos")
    String path,
    @Schema(example = "550e8400-e29b-41d4-a716-446655440000")
    String requestId
) {
}
