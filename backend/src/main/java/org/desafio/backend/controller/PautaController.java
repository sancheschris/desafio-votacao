package org.desafio.backend.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
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

@Tag(name = "Pautas", description = "Operações relacionadas a pautas")
@RestController
@RequestMapping("/api/v1/pautas")
public class PautaController {
    private final PautaService pautaService;

    public PautaController(PautaService pautaService) {
        this.pautaService = pautaService;
    }

    @Operation(summary = "Criar pauta", description = "Cadastra uma nova pauta para votação.")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Pauta criada"),
            @ApiResponse(responseCode = "400", description = "Validação inválida")
    })
    @PostMapping
    public ResponseEntity<PautaResponse> criarPauta(@Valid @RequestBody PautaRequest pautaRequest) {
        Pauta pauta = pautaService.createPauta(pautaRequest);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new PautaResponse(pauta.getId(), pauta.getTitulo(), pauta.getCreatedAt()));
    }
}
