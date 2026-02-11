package org.desafio.backend.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
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
import org.hibernate.annotations.CreationTimestamp;


@Entity
@Table(name = "pauta", schema = "votacao")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Pauta {
    @Id
    @GeneratedValue
    private UUID id;
    @Column(nullable = false)
    private String titulo;
    @CreationTimestamp
    @Column(name = "created_at",  nullable = false, updatable = false)
    private Instant createdAt;
}
