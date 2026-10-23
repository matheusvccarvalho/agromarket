package com.agromarket.api.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record ProducerRequestDTO(
    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 150, message = "O nome deve ter no máximo 150 caracteres")
    String name,

    @NotBlank(message = "O documento é obrigatório")
    @Size(max = 20, message = "O documento deve ter no máximo 20 caracteres")
    String document,

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "O formato do e-mail é inválido")
    @Size(max = 100, message = "O e-mail deve ter no máximo 100 caracteres")
    String email,

    @NotBlank(message = "O telefone é obrigatório")
    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    String phone
) {}