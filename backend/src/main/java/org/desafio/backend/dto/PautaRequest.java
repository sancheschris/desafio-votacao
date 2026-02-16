package org.desafio.backend.dto;


import jakarta.validation.constraints.NotBlank;

public record PautaRequest(
        @NotBlank(message = "O título da pauta é obrigatório.")
        String titulo
) {}
