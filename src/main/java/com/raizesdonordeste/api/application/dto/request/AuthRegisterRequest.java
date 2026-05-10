package com.raizesdonordeste.api.application.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record AuthRegisterRequest(
    @NotBlank(message = "nome é obrigatório")
    @Schema(example = "Cliente Teste")
    String nome,
    @NotBlank(message = "email é obrigatório")
    @Email(message = "email inválido")
    @Schema(example = "cliente.teste@raizes.com")
    String email,
    @NotBlank(message = "senha é obrigatória")
    @Size(min = 8, message = "senha deve ter ao menos 8 caracteres")
    @Schema(example = "Cliente@123")
    String senha,
    @NotNull(message = "consentimentoLGPD é obrigatório")
    @Schema(example = "true")
    Boolean consentimentoLGPD
) {
}
