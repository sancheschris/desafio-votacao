package org.desafio.backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.exception.ResourceConflictException;
import org.desafio.backend.exception.ResourceNotFoundException;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class SessaoVotacaoService {

    private final PautaRepository pautaRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;

    public SessaoVotacaoService(PautaRepository pautaRepository, SessaoVotacaoRepository sessaoVotacaoRepository) {
        this.pautaRepository = pautaRepository;
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
    }

    @Transactional
    public SessaoVotacao abrirSessaoVotacao(UUID pautaId, Integer durationInMinutes) {
        log.info("Abrindo sessão de votação para pauta ID: {} com duração: {} minutos", pautaId, durationInMinutes);

        Pauta pauta = pautaRepository.findById(pautaId)
                .orElseThrow(() ->  {
                    log.warn("Pauta não encontrada com ID: {}", pautaId);
                    return new ResourceNotFoundException("Pauta não encontrada com ID: " + pautaId);
                });

        long duration = (durationInMinutes == null || durationInMinutes <= 0)
                ? 1
                : durationInMinutes;

        Instant now = Instant.now();

        if (sessaoVotacaoRepository.findByPautaId(pauta.getId()).isPresent()) {
            log.warn("Já existe uma sessão de votação para a pauta ID: {}", pautaId);
            throw new ResourceConflictException("Já existe uma sessão para essa pauta.");
        }

        SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(now)
                .closesAt(now.plus(duration, ChronoUnit.MINUTES))
                .build();

        try {
            SessaoVotacao votacaoSaved = sessaoVotacaoRepository.save(sessaoVotacao);
            log.info("Sessão de votação aberta com ID: {} para pauta ID: {}", votacaoSaved.getId(), pautaId);
            return votacaoSaved;
        } catch (DataIntegrityViolationException e) {
            log.error("Erro ao abrir sessão de votação para pauta ID: {}: {}", pautaId, e.getMessage(), e);
            throw new ResourceConflictException("Já existe uma sessão para essa pauta.");
        }
    }
}
