package org.desafio.backend.controller;

import java.time.Instant;
import java.util.UUID;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.dto.AbrirSessaoRequest;
import org.desafio.backend.dto.SessaoVotacaoResponse;
import org.desafio.backend.service.SessaoVotacaoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/sessoes")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @PostMapping("/abrir/{pautaId}")
    public ResponseEntity<SessaoVotacaoResponse> abrirSessaoVotacao(@PathVariable UUID pautaId, @RequestBody AbrirSessaoRequest request) {
        SessaoVotacao sessao = sessaoVotacaoService.abrirSessaoVotacao(pautaId, request.durationInMinutes());

        String status =
                sessao.getClosedAt() != null ? "CLOSED"
                        : (Instant.now().isAfter(sessao.getClosesAt()) ? "EXPIRED" : "OPEN");

        SessaoVotacaoResponse sessaoVotacao = new SessaoVotacaoResponse(
                sessao.getId(),
                sessao.getPautaId(),
                sessao.getOpenedAt(),
                sessao.getClosesAt(),
                sessao.getClosedAt(),
                status
        );

        return ResponseEntity.status(HttpStatus.CREATED).body(sessaoVotacao);
    }
}
