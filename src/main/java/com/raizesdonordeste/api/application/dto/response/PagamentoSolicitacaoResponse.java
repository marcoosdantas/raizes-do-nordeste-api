package com.raizesdonordeste.api.application.dto.response;

public record PagamentoSolicitacaoResponse(
    String pagamentoId,
    String status,
    String mensagem
) {
}

