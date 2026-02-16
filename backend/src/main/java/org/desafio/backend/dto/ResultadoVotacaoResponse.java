package org.desafio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.util.UUID;

public record ResultadoVotacaoResponse(
        @Schema(example = "18f18d9a-a830-43c3-855b-02996819b317")
        UUID pautaId,
        @Schema(example = "10")
        long sim,
        @Schema(example = "7")
        long nao,
        @Schema(example = "SIM", description = "SIM | NAO | EMPATE")
        String resultado
) {
}
