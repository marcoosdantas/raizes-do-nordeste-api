package com.raizesdonordeste.api.application.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;

public record ProdutoCreateRequest(
    @NotBlank(message = "nome é obrigatório")
    String nome,
    @NotBlank(message = "descricao é obrigatória")
    String descricao,
    @NotBlank(message = "categoria é obrigatória")
    String categoria,
    @NotNull(message = "preco é obrigatório")
    @DecimalMin(value = "0.0", inclusive = false, message = "preco deve ser maior que zero")
    BigDecimal preco,
    @NotNull(message = "disponivel é obrigatório")
    Boolean disponivel,
    @NotNull(message = "sazonal é obrigatório")
    Boolean sazonal,
    String periodoDisponivel
) {
}

