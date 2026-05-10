package com.raizesdonordeste.api.application.dto.response;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(
    @Schema(example = "eyJhbGciOiJIUzI1NiJ9.eyJzdWIiOiJjbGllbnRlQHJhaXplcy5jb20ifQ.assinatura")
    String accessToken,
    @Schema(example = "Bearer")
    String tipo,
    @Schema(example = "3600000")
    long expiresIn,
    UsuarioResumoResponse usuario
) {
}
