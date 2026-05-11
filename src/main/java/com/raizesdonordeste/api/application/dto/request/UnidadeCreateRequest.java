package com.raizesdonordeste.api.application.dto.request;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UnidadeCreateRequest(
    @NotBlank(message = "nome é obrigatório")
    @Schema(example = "Raízes Recife Centro")
    String nome,
    @NotBlank(message = "cidade é obrigatória")
    @Schema(example = "Recife")
    String cidade,
    @NotBlank(message = "estado é obrigatório")
    @Schema(example = "PE")
    String estado,
    @NotNull(message = "ativa é obrigatória")
    @Schema(example = "true")
    Boolean ativa,
    @NotNull(message = "formato é obrigatório")
    @Schema(example = "COMPLETO")
    FormatoOperacao formato,
    @NotBlank(message = "horarioAbertura é obrigatório")
    @Schema(example = "08:00")
    String horarioAbertura,
    @NotBlank(message = "horarioFechamento é obrigatório")
    @Schema(example = "22:00")
    String horarioFechamento
) {
}
