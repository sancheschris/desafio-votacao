package org.desafio.backend.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.desafio.backend.domain.VotoValor;

public record CriarVotoRequest(
    @NotBlank String associadoId,
    @NotNull VotoValor voto) {
}
