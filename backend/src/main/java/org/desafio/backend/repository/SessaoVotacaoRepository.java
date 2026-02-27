package org.desafio.backend.repository;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.desafio.backend.domain.SessaoVotacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface SessaoVotacaoRepository extends JpaRepository<SessaoVotacao, UUID> {
    Optional<SessaoVotacao> findByPautaId(UUID pautaId);
    @Query("""
           select s from SessaoVotacao s
           where s.closedAt is null
             and s.closesAt <= :now
           """)
    List<SessaoVotacao> findExpiredOpenSessions(Instant now);
}
