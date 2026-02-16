package org.desafio.backend.service;

import java.time.Instant;
import java.util.UUID;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.domain.Voto;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.exception.AssociadoJaVotouException;
import org.desafio.backend.exception.ResourceNotFoundException;
import org.desafio.backend.exception.SessaoEncerradaException;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.desafio.backend.repository.VotoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.Instant.now;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public VotoService(VotoRepository votoRepository,
                       SessaoVotacaoRepository sessaoVotacaoRepository) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
    }

    @Transactional
    public Voto votar(UUID pautaId, String associadoId, VotoValor valor) {
        SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Sessão de votação não encontrada para esta pauta."));

        Instant now = now();

        if (sessao.getClosedAt() != null || !now.isBefore(sessao.getClosesAt())) {
            throw new SessaoEncerradaException("A sessão de votação para esta pauta já está encerrada.");
        }

        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, associadoId)) {
            throw new AssociadoJaVotouException("Associado já votou nesta pauta.");
        }

        Voto voto = Voto.builder()
                .pautaId(pautaId)
                .associadoId(associadoId)
                .valor(valor)
                .createdAt(now)
                .build();

        try {
            return votoRepository.save(voto);
        } catch (DataIntegrityViolationException ex) {
            throw new AssociadoJaVotouException("Associado já votou nesta pauta." + ex);
        }
    }
}
