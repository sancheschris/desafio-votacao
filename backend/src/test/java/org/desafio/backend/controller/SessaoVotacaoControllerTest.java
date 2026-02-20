package org.desafio.backend.controller;

import java.util.UUID;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.repository.PautaRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
class SessaoVotacaoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PautaRepository pautaRepository;

    @Test
    void shouldCreateSessaoVotacaoSuccessfullyAndReturn201() throws Exception {
        Pauta pauta = Pauta.builder()
                .titulo("Pauta para Sessão de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        String sessaoVotacaoJson = "{\"pautaId\": \"" + pauta.getId() + "\", \"duracao\": 10}";

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pauta.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessaoVotacaoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.pautaId").value(pauta.getId().toString()));
    }

    @Test
    void shouldReturn404WhenPautaNotFound() throws Exception {
        UUID nonExistentPautaId = UUID.randomUUID();
        String sessaoVotacaoJson = "{\"pautaId\": \"" + nonExistentPautaId + "\", \"duracao\": 10}";

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", nonExistentPautaId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessaoVotacaoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Pauta não encontrada com ID: " + nonExistentPautaId));
    }

    @Test
    void shouldReturn409WhenSessaoAlreadyExistsForPauta() throws Exception {
        Pauta pauta = Pauta.builder()
                .titulo("Pauta para Sessão de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        String sessaoVotacaoJson = "{\"pautaId\": \"" + pauta.getId() + "\", \"duracao\": 10}";

        // Criar a primeira sessão de votação
        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pauta.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessaoVotacaoJson))
                .andExpect(status().isCreated());

        // Tentar criar uma segunda sessão para a mesma pauta
        mockMvc.perform(post("/api/v1/pautas/{pautaId}/sessoes", pauta.getId())
                .contentType(MediaType.APPLICATION_JSON)
                .content(sessaoVotacaoJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Já existe uma sessão para essa pauta."));
    }
}