package org.desafio.backend.service;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.util.UUID;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.domain.Voto;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.desafio.backend.repository.VotoRepository;
import org.springframework.stereotype.Service;

@Service
public class VotoService {

    private final VotoRepository votoRepository;
    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public VotoService(VotoRepository votoRepository,
                       PautaRepository pautaRepository,
                       SessaoVotacaoRepository sessaoVotacaoRepository) {
        this.votoRepository = votoRepository;
        this.pautaRepository = pautaRepository;
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
    }

    @Transactional
    public Voto votar(UUID pautaId, String associadoId, VotoValor valor) {
        pautaRepository.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada."));

        SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> new IllegalStateException("Sessão de votação não encontrada para esta pauta."));

        Instant now = Instant.now();

        if (sessao.getClosedAt() != null || !now.isBefore(sessao.getClosesAt())) {
            throw new IllegalStateException("A sessão de votação para esta pauta já está encerrada.");
        }

        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, associadoId)) {
            throw new IllegalStateException("Associado já votou nesta pauta.");
        }

        Voto voto = Voto.builder()
                .pautaId(pautaId)
                .associadoId(associadoId)
                .valor(valor)
                .createdAt(now)
                .build();

        try {
            return votoRepository.save(voto);
        } catch (Exception e) {
            throw new RuntimeException("Erro ao registrar o voto: " + e.getMessage(), e);
        }
    }
}
