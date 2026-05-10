package com.raizesdonordeste.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record AuthLoginRequest(
    @NotBlank(message = "email é obrigatório")
    @Email(message = "email inválido")
    @Schema(example = "cliente@raizes.com")
    String email,
    @NotBlank(message = "senha é obrigatória")
    @Schema(example = "Cliente@123")
    String senha
) {
}
