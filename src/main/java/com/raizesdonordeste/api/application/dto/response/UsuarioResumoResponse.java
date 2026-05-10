package com.raizesdonordeste.api.application.dto.response;

import com.raizesdonordeste.api.domain.enums.PerfilUsuario;
import io.swagger.v3.oas.annotations.media.Schema;

public record UsuarioResumoResponse(
    @Schema(example = "665f1f8a2c1b2a0012ab3456")
    String id,
    @Schema(example = "Cliente")
    String nome,
    @Schema(example = "cliente@raizes.com")
    String email,
    @Schema(example = "CLIENTE")
    PerfilUsuario perfil
) {
}
