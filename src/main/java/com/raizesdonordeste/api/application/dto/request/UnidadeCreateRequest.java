package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnidadeCreateRequest(
    @NotBlank(message = "nome é obrigatório")
    String nome,
    @NotBlank(message = "cidade é obrigatória")
    String cidade,
    @NotBlank(message = "estado é obrigatório")
    String estado,
    @NotNull(message = "ativa é obrigatória")
    Boolean ativa,
    @NotNull(message = "formato é obrigatório")
    FormatoOperacao formato,
    @NotBlank(message = "horarioAbertura é obrigatório")
    String horarioAbertura,
    @NotBlank(message = "horarioFechamento é obrigatório")
    String horarioFechamento
) {
}

