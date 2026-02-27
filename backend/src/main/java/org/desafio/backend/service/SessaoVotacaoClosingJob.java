package org.desafio.backend.service;

import java.time.Instant;
import java.util.List;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.dto.ResultadoVotacaoResponse;
import org.desafio.backend.integration.dto.ResultadoVotacaoEvent;
import org.desafio.backend.integration.messaging.ResultadoVotacaoPublisher;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class SessaoVotacaoClosingJob {
    private final SessaoVotacaoRepository sessaoRepo;
    private final ResultadoVotacaoService resultadoService;
    private final ResultadoVotacaoPublisher publisher;

    public SessaoVotacaoClosingJob(
            SessaoVotacaoRepository sessaoRepo,
            ResultadoVotacaoService resultadoService,
            ResultadoVotacaoPublisher publisher
    ) {
        this.sessaoRepo = sessaoRepo;
        this.resultadoService = resultadoService;
        this.publisher = publisher;
    }

    @Scheduled(fixedDelayString = "${votacao.session-close-job-ms:5000}")
    @Transactional
    public void closeExpiredSessions() {
        Instant now = Instant.now();

        List<SessaoVotacao> expiradas = sessaoRepo.findExpiredOpenSessions(now);

        for (SessaoVotacao sessao : expiradas) {
            sessao.setClosedAt(now);

            ResultadoVotacaoResponse resultadoVotacao = resultadoService.resultado(sessao.getPautaId());
            publisher.publish(new ResultadoVotacaoEvent(
                    sessao.getPautaId(),
                    sessao.getId(),
                    resultadoVotacao.sim(),
                    resultadoVotacao.nao(),
                    resultadoVotacao.resultado(),
                    now
            ));
        }
    }
}
