package org.desafio.backend.controller;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PautaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void shouldCreatePautaSuccessfullyAndReturn201() throws Exception {
        String pautaJson = "{\"titulo\": \"Pauta de Teste\", \"descricao\": \"Descrição da pauta de teste\"}";

        mockMvc.perform(post("/api/v1/pautas")
                .contentType("application/json")
                .content(pautaJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.titulo").value("Pauta de Teste"));
    }

    @Test
    void shouldReturn400WhenTituloIsMissing() throws Exception {
        String pautaJson = "{\"descricao\": \"Descrição da pauta sem título\"}";

        mockMvc.perform(post("/api/v1/pautas")
                .contentType("application/json")
                .content(pautaJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("titulo: O título da pauta é obrigatório."));
    }
}