package org.desafio.backend.service;

import jakarta.transaction.Transactional;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

@Service
public class SessaoVotacaoService {

    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public SessaoVotacaoService(PautaRepository pautaRepository, SessaoVotacaoRepository sessaoVotacaoRepository) {
        this.pautaRepository = pautaRepository;
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
    }

    @Transactional
    public SessaoVotacao abrirSessaoVotacao(UUID pautaId, Integer durationInMinutes) {
        var pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() -> new IllegalArgumentException("Pauta não encontrada com ID: " + pautaId));

        long duration = (durationInMinutes == null || durationInMinutes <= 0)
                ? 1
                : durationInMinutes;

        Instant now = Instant.now();

        SessaoVotacao session = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(now)
                .closesAt(now.plus(duration, ChronoUnit.MINUTES))
                .build();

        try {
            return sessaoVotacaoRepository.save(session);
        } catch (DataIntegrityViolationException e) {
            throw new IllegalStateException("Já existe uma sessão para essa pauta.");
        }
    }
}
