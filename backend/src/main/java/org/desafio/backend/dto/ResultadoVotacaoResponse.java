package org.desafio.backend.dto;

import java.util.UUID;

public record ResultadoVotacaoResponse(
        UUID pautaId,
        long sim,
        long nao,
        String resultado
) {
}
