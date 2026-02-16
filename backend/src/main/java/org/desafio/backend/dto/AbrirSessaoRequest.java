package org.desafio.backend.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AbrirSessaoRequest(
        @Schema(example = "1", description = "Duração em minutos (default 1 se null)")
        Integer durationInMinutes) {
}
