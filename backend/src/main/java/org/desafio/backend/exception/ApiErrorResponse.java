package org.desafio.backend.exception;

import lombok.Builder;

import java.time.Instant;

@Builder
public record ApiErrorResponse(
        String message,
        Instant timestamp,
        int status,
        String path
) {}