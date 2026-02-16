package org.desafio.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

@Tag(name = "Sessões", description = "Operações de sessão de votação")
@RestController
@RequestMapping("/api/v1/pautas")
public class SessaoVotacaoController {

    private final SessaoVotacaoService sessaoVotacaoService;

    public SessaoVotacaoController(SessaoVotacaoService sessaoVotacaoService) {
        this.sessaoVotacaoService = sessaoVotacaoService;
    }

    @Operation(summary = "Abrir sessão de votação",
            description = "Abre uma sessão para uma pauta. Duração padrão: 1 minuto.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Sessão aberta"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada"),
            @ApiResponse(responseCode = "409", description = "Sessão já existe para a pauta")
    })
    @PostMapping("/{pautaId}/sessoes")
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
