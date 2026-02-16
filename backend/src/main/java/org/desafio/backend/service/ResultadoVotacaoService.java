package org.desafio.backend.service;

import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.dto.ResultadoVotacaoResponse;
import org.desafio.backend.exception.ResourceNotFoundException;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.VotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
public class ResultadoVotacaoService {

    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;

    public ResultadoVotacaoService(PautaRepository pautaRepository, VotoRepository votoRepository) {
        this.pautaRepository = pautaRepository;
        this.votoRepository = votoRepository;
    }

    @Transactional(readOnly = true)
    public ResultadoVotacaoResponse resultado(UUID pautaId) {
        log.info("Calculando resultado da votação para pauta ID: {}", pautaId);

        pautaRepository.findById(pautaId)
                .orElseThrow(() -> {
                    log.warn("Pauta não encontrada com ID: {}", pautaId);
                    return new ResourceNotFoundException("Pauta não encontrada: " + pautaId);
                });

        long totalVotosSim = votoRepository.countByPautaIdAndValor(pautaId, VotoValor.SIM);
        long totalVotosNao = votoRepository.countByPautaIdAndValor(pautaId, VotoValor.NAO);


        String resultadoFinal;
        if (totalVotosSim > totalVotosNao) {
            resultadoFinal = "SIM";
        }
        else if (totalVotosNao > totalVotosSim) {
            resultadoFinal = "NAO";
        }
        else {
            resultadoFinal = "EMPATE";
        }

        log.info("Resultado da votação para pauta ID: {} - SIM: {}, NAO: {}, Resultado Final: {}", pautaId, totalVotosSim, totalVotosNao, resultadoFinal);

        return new ResultadoVotacaoResponse(pautaId, totalVotosSim, totalVotosNao, resultadoFinal);
    }
}
