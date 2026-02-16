package org.desafio.backend.controller;

import java.util.UUID;
import org.desafio.backend.domain.Voto;
import org.desafio.backend.dto.CriarVotoRequest;
import org.desafio.backend.dto.ResultadoVotacaoResponse;
import org.desafio.backend.dto.VotoResponse;
import org.desafio.backend.service.ResultadoVotacaoService;
import org.desafio.backend.service.VotoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/pautas")
public class VotoController {

    private final VotoService votoService;
    private final ResultadoVotacaoService resultadoVotacaoService;

    public VotoController(VotoService votoService, ResultadoVotacaoService resultadoVotacaoService) {
        this.votoService = votoService;
        this.resultadoVotacaoService = resultadoVotacaoService;
    }

    @PostMapping("/{pautaId}/votos")
    public ResponseEntity<VotoResponse> votar(@PathVariable UUID pautaId, @RequestBody CriarVotoRequest request) {

        Voto voto = votoService.votar(pautaId, request.associadoId(), request.voto());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(new VotoResponse(
                        voto.getId(),
                        voto.getPautaId(),
                        voto.getAssociadoId(),
                        voto.getValor(),
                        voto.getCreatedAt()
                ));
    }

    @GetMapping("/{pautaId}/resultado")
    public ResponseEntity<ResultadoVotacaoResponse> resultadoVotacao(@PathVariable UUID pautaId) {
        return ResponseEntity.ok(resultadoVotacaoService.resultado(pautaId));
    }
}
