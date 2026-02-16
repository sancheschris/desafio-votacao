package org.desafio.backend.dto;

import org.desafio.backend.domain.VotoValor;

public record CriarVotoRequest(
    String associadoId,
    VotoValor voto) {
}
