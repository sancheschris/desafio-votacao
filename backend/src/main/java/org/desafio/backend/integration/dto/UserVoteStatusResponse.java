package org.desafio.backend.integration.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserVoteStatusResponse(
        @Schema(description = "Status do CPF para votação. Pode ser 'ABLE_TO_VOTE' ou 'UNABLE_TO_VOTE'.")
        String status) {}
