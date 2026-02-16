package org.desafio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import java.time.Instant;
import java.util.UUID;

public record PautaResponse (
        @Schema(example = "18f18d9a-a830-43c3-855b-02996819b317")
        UUID id,
        @Schema(example = "Aprovar orçamento 2026")
        String titulo,
        @Schema(example = "2026-02-16T18:26:23.234044Z")
        Instant createdAt) {
}
