package org.desafio.backend.dto;

import java.time.Instant;
import java.util.UUID;

public record SessaoVotacaoResponse(UUID id,
                                    UUID pautaId,
                                    Instant openedAt,
                                    Instant closesAt,
                                    Instant closedAt,
                                    String status) {
}
