package org.desafio.backend.dto;

import java.time.Instant;
import java.util.UUID;

public record PautaResponse (UUID id, String titulo, Instant createdAt) {
}
