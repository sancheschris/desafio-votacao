package org.desafio.backend.domain;

import java.time.Instant;
import java.util.UUID;
import lombok.Builder;
import lombok.Data;
import lombok.extern.jackson.Jacksonized;

@Jacksonized
@Builder
@Data
public class Voto {
    private UUID id;
    private UUID pautaId;
    private String cpf;
    private VotoValor valor;
    private Instant createdAt;
}
