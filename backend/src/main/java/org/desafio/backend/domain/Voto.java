package org.desafio.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.Instant;
import java.util.UUID;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "voto", schema = "votacao")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Voto {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(name = "pauta_id", nullable = false)
    private UUID pautaId;
    @Column(name = "associado_id", nullable = false)
    private String associadoId;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private VotoValor valor;
    @Column(name = "created_at", nullable = false, updatable = false, insertable = false)
    private Instant createdAt;
}
