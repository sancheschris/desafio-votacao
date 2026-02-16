package org.desafio.backend.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.Instant;

@Builder
@Schema(name = "ApiErrorResponse", description = "Padrão de retorno para erros da API")
public record ApiErrorResponse(
        @Schema(example = "Associado já votou nesta pauta.", description = "Mensagem descritiva do erro")
        String message,
        @Schema(example = "2026-02-16T20:15:30Z", description = "Data e hora em que o erro ocorreu")
        Instant timestamp,
        @Schema(example = "409", description = "Código HTTP do erro")
        int status,
        @Schema(example = "/votos/18f18d9a-a830-43c3-855b-02996819b317", description = "Endpoint que gerou o erro")
        String path
) {}