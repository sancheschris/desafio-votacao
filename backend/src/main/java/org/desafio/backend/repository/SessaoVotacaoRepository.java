package org.desafio.backend.repository;

import java.util.Optional;
import java.util.UUID;
import org.desafio.backend.domain.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, UUID> {
    Optional<SessaoVotacao> findByPautaId(UUID pautaId);
}
