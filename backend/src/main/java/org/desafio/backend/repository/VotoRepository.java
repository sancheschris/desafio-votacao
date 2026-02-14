package org.desafio.backend.repository;

import java.util.UUID;
import org.desafio.backend.domain.Voto;
import org.desafio.backend.domain.VotoValor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VotoRepository extends JpaRepository<Voto, UUID> {
    boolean existsByPautaIdAndAssociadoId(UUID pautaId, String associadoId);
    long countByPautaIdAndValor(UUID pautaId, VotoValor valor);
}
