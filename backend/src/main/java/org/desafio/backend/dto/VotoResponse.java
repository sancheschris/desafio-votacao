package org.desafio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;
import org.desafio.backend.domain.VotoValor;

public record VotoResponse(
        @Schema(example = "18f18d9a-a830-43c3-855b-02996819b317")
        UUID id,
        @Schema(example = "28f18d9a-a830-43c3-855b-02996819b317")
        UUID pautaId,
        @Schema(example = "432.456.789-00", description = "CPF do associado")
        String associadoId,
        @Schema(example = "SIM", description = "Valor do voto: SIM ou NAO")
        VotoValor valor,
        @Schema(example = "2026-02-16T18:26:23.234044Z")
        Instant createdAt
) {
}
