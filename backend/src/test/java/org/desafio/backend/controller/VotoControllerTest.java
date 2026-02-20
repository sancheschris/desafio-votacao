package org.desafio.backend.controller;

import java.time.Instant;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.domain.Voto;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.integration.cpf.CpfValidationClient;
import org.desafio.backend.integration.cpf.FakeCpfValidationClient;
import org.desafio.backend.integration.cpf.StatusCpf;
import org.desafio.backend.integration.dto.UserVoteStatusResponse;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.desafio.backend.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class VotoControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private PautaRepository pautaRepository;
    @Autowired
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    @Autowired
    private VotoRepository votoRepository;
    @MockitoBean
    private FakeCpfValidationClient cpfValidationClient;

    @Test
    void shouldCreateVotoSuccessfullyAndReturn201() throws Exception {
        Mockito.when(cpfValidationClient.checkCpf("128.955.940-66"))
                .thenReturn(new UserVoteStatusResponse(StatusCpf.ABLE_TO_VOTE.name()));

        Pauta pauta = Pauta.builder()
                .titulo("Pauta de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(Instant.now())
                .closesAt(Instant.now().plusSeconds(60))
                .build();
        sessaoVotacaoRepository.save(sessao);

        String associadoId = "128.955.940-66";
        String votoJson = "{\"associadoId\": \"" + associadoId+ "\", \"voto\": \"SIM\"}";

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.associadoId").value(associadoId))
                .andExpect(jsonPath("$.valor").value("SIM"));
    }

    @Test
    void shouldReturn400WhenAssociadoIdIsMissing() throws Exception {
        Pauta pauta = Pauta.builder()
                .titulo("Pauta de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(Instant.now())
                .closesAt(Instant.now().plusSeconds(60))
                .build();
        sessaoVotacaoRepository.save(sessao);

        String votoJson = "{\"voto\": \"SIM\"}";

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("associadoId: must not be blank"));
    }

    @Test
    void shouldReturn400WhenVotoIsMissing() throws Exception {
        Pauta pauta = Pauta.builder()
                .titulo("Pauta de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(Instant.now())
                .closesAt(Instant.now().plusSeconds(60))
                .build();
        sessaoVotacaoRepository.save(sessao);

        String associadoId = "128.955.940-66";
        String votoJson = "{\"associadoId\": \"" + associadoId + "\"}";

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("voto: must not be null"));
    }

    @Test
    void shouldReturn400WhenCpfIsInvalid() throws Exception {
        Mockito.when(cpfValidationClient.checkCpf("128.955.940-66"))
                .thenReturn(new UserVoteStatusResponse(StatusCpf.UNABLE_TO_VOTE.name()));

        Pauta pauta = Pauta.builder()
                .titulo("Pauta de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(Instant.now())
                .closesAt(Instant.now().plusSeconds(60))
                .build();
        sessaoVotacaoRepository.save(sessao);

        String associadoId = "128.955.940-66";
        String votoJson = "{\"associadoId\": \"" + associadoId + "\", \"voto\": \"SIM\"}";

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Usuário não pode votar."));
    }

    @Test
    void shouldReturn404WhenPautaNotFound() throws Exception {
        String associadoId = "128.955.940-66";
        String votoJson = "{\"associadoId\": \"" + associadoId + "\", \"voto\": \"SIM\"}";

        Mockito.when(cpfValidationClient.checkCpf("128.955.940-66"))
                .thenReturn(new UserVoteStatusResponse(StatusCpf.ABLE_TO_VOTE.name()));

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", "00000000-0000-0000-0000-000000000000")
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Sessão de votação não encontrada para esta pauta."));
    }

    @Test
    void shouldReturn409WhenSessaoEncerrada() throws Exception {
        Pauta pauta = Pauta.builder()
                .titulo("Pauta de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(Instant.now().minusSeconds(120))
                .closesAt(Instant.now().minusSeconds(60))
                .build();
        sessaoVotacaoRepository.save(sessao);

        String associadoId = "128.955.940-66";
        String votoJson = "{\"associadoId\": \"" + associadoId + "\", \"voto\": \"SIM\"}";

        Mockito.when(cpfValidationClient.checkCpf("128.955.940-66"))
                .thenReturn(new UserVoteStatusResponse(StatusCpf.ABLE_TO_VOTE.name()));

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("A sessão de votação para esta pauta já está encerrada."));
    }

    @Test
    void shouldReturn409WhenAssociadoAlreadyVoted() throws Exception {
        Pauta pauta = Pauta.builder()
                .titulo("Pauta de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        SessaoVotacao sessao = SessaoVotacao.builder()
                .pautaId(pauta.getId())
                .openedAt(Instant.now())
                .closesAt(Instant.now().plusSeconds(60))
                .build();
        sessaoVotacaoRepository.save(sessao);

        String associadoId = "128.955.940-66";
        String votoJson = "{\"associadoId\": \"" + associadoId + "\", \"voto\": \"SIM\"}";

        Mockito.when(cpfValidationClient.checkCpf("128.955.940-66"))
                .thenReturn(new UserVoteStatusResponse(StatusCpf.ABLE_TO_VOTE.name()));

        // Primeiro voto
        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isCreated());

        // Segundo voto do mesmo associado
        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.message").value("Associado já votou nesta pauta."));
    }

    @Test
    void shouldReturn404WhenSessaoNotFound() throws Exception {
        Pauta pauta = Pauta.builder()
                .titulo("Pauta de Teste")
                .build();
        pauta = pautaRepository.save(pauta);

        String associadoId = "128.955.940-66";
        String votoJson = "{\"associadoId\": \"" + associadoId + "\", \"voto\": \"SIM\"}";

        Mockito.when(cpfValidationClient.checkCpf("128.955.940-66"))
                .thenReturn(new UserVoteStatusResponse(StatusCpf.ABLE_TO_VOTE.name()));

        mockMvc.perform(post("/api/v1/pautas/{pautaId}/votos", pauta.getId())
                .contentType("application/json")
                .content(votoJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("Sessão de votação não encontrada para esta pauta."));
    }
}