package org.desafio.backend.controller;

import org.desafio.backend.domain.Pauta;
import org.desafio.backend.dto.PautaRequest;
import org.desafio.backend.dto.PautaResponse;
import org.desafio.backend.service.PautaService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/pautas")
public class PautaController {
    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @PostMapping
    public ResponseEntity<PautaResponse> criarPauta(@RequestBody PautaRequest pautaRequest) {
        Pauta pauta = pautaService.createPauta(pautaRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PautaResponse(pauta.getId(), pauta.getTitulo(), pauta.getCreatedAt()));
    }
}
