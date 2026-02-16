package org.desafio.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "Votos", description = "Votos e apuração")
@RestController
@RequestMapping("/api/v1/pautas")
public class VotoController {

    private final VotoService votoService;
    private final ResultadoVotacaoService resultadoVotacaoService;

    public VotoController(VotoService votoService, ResultadoVotacaoService resultadoVotacaoService) {
        this.votoService = votoService;
        this.resultadoVotacaoService = resultadoVotacaoService;
    }

    @Operation(summary = "Registrar voto", description = "Vota SIM/NAO. Um voto por associado por pauta.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Voto registrado"),
            @ApiResponse(responseCode = "404", description = "Pauta/sessão não encontrada"),
            @ApiResponse(responseCode = "409", description = "Sessão encerrada ou associado já votou"),
            @ApiResponse(responseCode = "400", description = "Validação inválida")
    })
    @PostMapping("/{pautaId}/votos")
    public ResponseEntity<VotoResponse> votar(@PathVariable UUID pautaId, @Valid @RequestBody CriarVotoRequest request) {

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

    @Operation(summary = "Resultado da votação", description = "Retorna contagem de SIM/NAO e resultado final.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Resultado retornado"),
            @ApiResponse(responseCode = "404", description = "Pauta não encontrada")
    })
    @GetMapping("/{pautaId}/resultado")
    public ResponseEntity<ResultadoVotacaoResponse> resultadoVotacao(@PathVariable UUID pautaId) {
        return ResponseEntity.ok(resultadoVotacaoService.resultado(pautaId));
    }
}
