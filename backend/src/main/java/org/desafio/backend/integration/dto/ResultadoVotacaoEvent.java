package org.desafio.backend.integration.dto;

import java.time.Instant;
import java.util.UUID;

public record ResultadoVotacaoEvent(
         UUID pautaId,
         UUID sessaoId,
         long sim,
         long nao,
         String resultado,
         Instant closedAt) {
}
