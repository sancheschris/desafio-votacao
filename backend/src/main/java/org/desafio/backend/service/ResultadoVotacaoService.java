package org.desafio.backend.service;

import java.util.UUID;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.dto.ResultadoVotacaoResponse;
import org.desafio.backend.exception.ResourceNotFoundException;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.VotoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ResultadoVotacaoService {

    private final PautaRepository pautaRepository;
    private final VotoRepository votoRepository;

    public ResultadoVotacaoService(PautaRepository pautaRepository, VotoRepository votoRepository) {
        this.pautaRepository = pautaRepository;
        this.votoRepository = votoRepository;
    }

    @Transactional(readOnly = true)
    public ResultadoVotacaoResponse resultado(UUID pautaId) {

        pautaRepository.findById(pautaId)
                .orElseThrow(() -> new ResourceNotFoundException("Pauta não encontrada: " + pautaId));

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

        return new ResultadoVotacaoResponse(pautaId, totalVotosSim, totalVotosNao, resultadoFinal);
    }
}
