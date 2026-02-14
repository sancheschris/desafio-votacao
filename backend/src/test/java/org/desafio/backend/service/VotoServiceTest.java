package org.desafio.backend.service;

import java.time.Instant;
import java.util.Optional;
import java.util.UUID;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.domain.Voto;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.desafio.backend.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class VotoServiceTest {

    @InjectMocks
    private VotoService votoService;
    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;
    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private VotoRepository votoRepository;

    @Test
    void testVotar() {
        Pauta pauta = Pauta.builder()
            .id(UUID.randomUUID())
            .titulo("Pauta de Teste")
            .createdAt(Instant.now())
            .build();

        SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
            .id(UUID.randomUUID())
            .pautaId(pauta.getId())
            .openedAt(Instant.now().minusSeconds(3600))
            .closesAt(Instant.now().plusSeconds(3600))
            .build();

        Voto expected = Voto.builder()
            .id(UUID.randomUUID())
            .pautaId(pauta.getId())
            .associadoId("128783291")
            .valor(VotoValor.SIM)
            .createdAt(Instant.now())
            .build();

        when(pautaRepository.findById(pauta.getId())).thenReturn(java.util.Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(pauta.getId())).thenReturn(Optional.of(sessaoVotacao));
        when(votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), "128783291")).thenReturn(false);
        when(votoRepository.save(any())).thenReturn(expected);

        Voto actual = votoService.votar(pauta.getId(), expected.getAssociadoId(), expected.getValor());

        assertEquals(expected, actual);
        verify(votoRepository, atLeastOnce()).save(any(Voto.class));
    }

    @Test
    void testVotarSessaoEncerrada() {
        Pauta pauta = Pauta.builder()
            .id(UUID.randomUUID())
            .titulo("Pauta de Teste")
            .createdAt(Instant.now())
            .build();

        SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
            .id(UUID.randomUUID())
            .pautaId(pauta.getId())
            .openedAt(Instant.now().minusSeconds(7200))
            .closesAt(Instant.now().minusSeconds(3600))
            .closedAt(Instant.now().minusSeconds(3600))
            .build();

        when(pautaRepository.findById(pauta.getId())).thenReturn(java.util.Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(pauta.getId())).thenReturn(Optional.of(sessaoVotacao));

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> votoService.votar(pauta.getId(), "128783291", VotoValor.SIM)
        );
        assertEquals("A sessão de votação para esta pauta já está encerrada.", exception.getMessage());
    }

    @Test
    void testVotarAssociadoJaVotou() {
        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta de Teste")
                .createdAt(Instant.now())
                .build();

        SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .pautaId(pauta.getId())
                .openedAt(Instant.now().minusSeconds(3600))
                .closesAt(Instant.now().plusSeconds(3600))
                .build();

        when(pautaRepository.findById(pauta.getId())).thenReturn(java.util.Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(pauta.getId())).thenReturn(Optional.of(sessaoVotacao));
        when(votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), "128783291")).thenReturn(true);

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> votoService.votar(pauta.getId(), "128783291", VotoValor.SIM)
        );
        assertEquals("Associado já votou nesta pauta.", exception.getMessage());
    }

    @Test
    void testVotarPautaNaoEncontrada() {
        UUID pautaId = UUID.randomUUID();
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(
                IllegalArgumentException.class,
                () -> votoService.votar(pautaId, "128783291", VotoValor.SIM)
        );
        assertEquals("Pauta não encontrada.", exception.getMessage());
    }

    @Test
    void testVotarSessaoVotacaoNaoEncontrada() {
        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta de Teste")
                .createdAt(Instant.now())
                .build();

        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(pauta.getId())).thenReturn(Optional.empty());

        IllegalStateException exception = assertThrows(
                IllegalStateException.class,
                () -> votoService.votar(pauta.getId(), "128783291", VotoValor.SIM)
        );
        assertEquals("Sessão de votação não encontrada para esta pauta.", exception.getMessage());
    }

    @Test
    void testVotarErroAoRegistrarVoto() {
        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta de Teste")
                .createdAt(Instant.now())
                .build();

        SessaoVotacao sessaoVotacao = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .pautaId(pauta.getId())
                .openedAt(Instant.now().minusSeconds(3600))
                .closesAt(Instant.now().plusSeconds(3600))
                .build();

        when(pautaRepository.findById(pauta.getId())).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.findByPautaId(pauta.getId())).thenReturn(Optional.of(sessaoVotacao));
        when(votoRepository.existsByPautaIdAndAssociadoId(pauta.getId(), "128783291")).thenReturn(false);
        when(votoRepository.save(any())).thenThrow(new RuntimeException("Erro de banco de dados"));

        RuntimeException exception = assertThrows(
                RuntimeException.class,
                () -> votoService.votar(pauta.getId(), "128783291", VotoValor.SIM)
        );
        assertEquals("Erro ao registrar o voto: Erro de banco de dados", exception.getMessage());
    }
}