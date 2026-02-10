package org.desafio.backend.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class Pauta {
    private UUID id;
    private String descricao;
    private Instant createdAt;
}
