package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

public record PedidoCreateRequest(
    @NotBlank(message = "unidadeId é obrigatório")
    @Schema(example = "665f1f8a2c1b2a0012ab1001")
    String unidadeId,
    @NotNull(message = "canalPedido é obrigatório")
    @Schema(example = "APP")
    CanalPedido canalPedido,
    @NotEmpty(message = "itens é obrigatório")
    @ArraySchema(schema = @Schema(implementation = PedidoItemRequest.class))
    List<@Valid PedidoItemRequest> itens,
    @NotBlank(message = "formaPagamento é obrigatória")
    @Schema(example = "PIX")
    String formaPagamento
) {
}
