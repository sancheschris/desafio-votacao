package org.desafio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record SessaoVotacaoResponse(
        @Schema(example = "6d5e8e6c-2e41-4e59-b70b-9a8a5c0d4f1c")
        UUID id,
        @Schema(example = "18f18d9a-a830-43c3-855b-02996819b317")
        UUID pautaId,
        @Schema(example = "2026-02-16T18:26:23.234044Z")
        Instant openedAt,
        @Schema(example = "2026-02-16T18:31:23.234044Z")
        Instant closesAt,
        @Schema(example = "2026-02-16T18:31:23.29Z")
        Instant closedAt,
        @Schema(example = "ABERTA", description = "Status da sessão: ABERTA, ENCERRADA ou EXPIRADA")
        String status) {
}
