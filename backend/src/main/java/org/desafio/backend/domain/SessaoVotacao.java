package org.desafio.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
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
@Table(name = "sessao_votacao", schema = "votacao")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class SessaoVotacao {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "pauta_id", nullable = false, updatable = false)
    private UUID pautaId;

    @Column(name = "opened_at", nullable = false)
    private Instant openedAt;

    @Column(name = "closes_at", nullable = false)
    private Instant closesAt;

    @Column(name = "closed_at")
    private Instant closedAt;

    public boolean isOpen() {
        return closedAt == null && Instant.now().isBefore(closesAt);
    }

    public void close() {
        this.closedAt = Instant.now();
    }
}
