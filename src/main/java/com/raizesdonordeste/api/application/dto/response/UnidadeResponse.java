package com.raizesdonordeste.api.application.dto.response;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;

public record UnidadeResponse(
    String id,
    String nome,
    String cidade,
    String estado,
    boolean ativa,
    FormatoOperacao formato,
    String horarioAbertura,
    String horarioFechamento
) {
}

