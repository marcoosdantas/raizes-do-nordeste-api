package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;

public record UnidadeUpdateRequest(
    String nome,
    String cidade,
    String estado,
    Boolean ativa,
    FormatoOperacao formato,
    String horarioAbertura,
    String horarioFechamento
) {
}

