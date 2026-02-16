package org.desafio.backend.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

public record PautaRequest(
        @NotBlank(message = "O título da pauta é obrigatório.")
        @Schema(example = "Aprovar orçamento 2026", description = "Título da pauta")
        String titulo
) {}
