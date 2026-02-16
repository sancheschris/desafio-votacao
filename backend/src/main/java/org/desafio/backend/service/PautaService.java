package org.desafio.backend.service;

import org.desafio.backend.domain.Pauta;
import org.desafio.backend.dto.PautaRequest;
import org.desafio.backend.repository.PautaRepository;
import org.springframework.stereotype.Service;

@Service
public class PautaService {

    private final PautaRepository pautaRepository;

    public PautaService(PautaRepository pautaRepository) {
        this.pautaRepository = pautaRepository;
    }

    public Pauta createPauta(PautaRequest pautaRequest) {
        Pauta novaPauta = Pauta.builder()
                .titulo(pautaRequest.titulo().trim())
                .build();

        return pautaRepository.save(novaPauta);
    }
}
