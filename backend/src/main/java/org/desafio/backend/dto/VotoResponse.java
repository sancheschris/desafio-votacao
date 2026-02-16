package org.desafio.backend.dto;

import java.time.Instant;
import java.util.UUID;
import org.desafio.backend.domain.VotoValor;

public record VotoResponse(
        UUID id,
        UUID pautaId,
        String associadoId,
        VotoValor valor,
        Instant createdAt
) {
}
