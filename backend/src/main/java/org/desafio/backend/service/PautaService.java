package org.desafio.backend.service;

import lombok.extern.slf4j.Slf4j;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.dto.PautaRequest;
import org.desafio.backend.repository.PautaRepository;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PautaService {

    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta createPauta(PautaRequest pautaRequest) {
        log.info("Criando nova pauta com título: {}", pautaRequest.titulo());

        Pauta novaPauta = Pauta.builder()
                .titulo(pautaRequest.titulo().trim())
                .build();

        try {
            novaPauta = pautaRepository.save(novaPauta);
            log.info("Pauta criada com ID: {}", novaPauta.getId());
        } catch (Exception e) {
            log.error("Erro ao criar pauta: {}", e.getMessage(), e);
            throw e;
        }
        return novaPauta;
    }
}
