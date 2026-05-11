package com.raizesdonordeste.api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import io.swagger.v3.oas.annotations.media.Schema;
import java.math.BigDecimal;

public record ProdutoCreateRequest(
    @NotBlank(message = "nome é obrigatório")
    @Schema(example = "Tapioca de Frango")
    String nome,
    @NotBlank(message = "descricao é obrigatória")
    @Schema(example = "Tapioca recheada com frango temperado e queijo coalho")
    String descricao,
    @NotBlank(message = "categoria é obrigatória")
    @Schema(example = "TAPIOCA")
    String categoria,
    @NotNull(message = "preco é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "preco deve ser maior que zero")
    @Schema(example = "14.90")
    BigDecimal preco,
    @NotNull(message = "disponivel é obrigatório")
    @Schema(example = "true")
    Boolean disponivel,
    @NotNull(message = "sazonal é obrigatório")
    @Schema(example = "false")
    Boolean sazonal,
    @Schema(example = "JUNINO")
    String periodoDisponivel
) {
}
