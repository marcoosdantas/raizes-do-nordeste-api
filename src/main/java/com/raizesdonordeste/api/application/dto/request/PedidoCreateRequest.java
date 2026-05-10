package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoCreateRequest(
    @NotBlank(message = "unidadeId é obrigatório")
    String unidadeId,
    @NotNull(message = "canalPedido é obrigatório")
    CanalPedido canalPedido,
    @NotEmpty(message = "itens é obrigatório")
    List<@Valid PedidoItemRequest> itens,
    @NotBlank(message = "formaPagamento é obrigatória")
    String formaPagamento
) {
}

