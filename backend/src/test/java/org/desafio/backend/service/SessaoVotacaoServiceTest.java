package org.desafio.backend.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Optional;
import java.util.UUID;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class SessaoVotacaoServiceTest {

    @InjectMocks
    private SessaoVotacaoService sessaoVotacaoService;
    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private SessaoVotacaoRepository sessaoVotacaoRepository;


    @Test
    public void testAbrirSessaoVotacao() {
        // Arrange
        Integer durationInMinutes = 3;
        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta de Teste")
                .build();

        Instant now = Instant.now();

        SessaoVotacao expected = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .pautaId(pauta.getId())
                .openedAt(now)
                .closesAt(now.plus(durationInMinutes, ChronoUnit.MINUTES))
                .closedAt(null)
                .build();

        when(pautaRepository.findById(any())).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any())).thenReturn(expected);

        // Act
        SessaoVotacao actual = sessaoVotacaoService.abrirSessaoVotacao(pauta.getId(), durationInMinutes);

        // Assert
        assertEquals(expected, actual);
        verify(sessaoVotacaoRepository, atLeastOnce()).save(any(SessaoVotacao.class));
    }

    @Test
    public void testAbrirSessaoVotacaoComDuracaoPadrao() {
        // Arrange
        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta de Teste")
                .build();

        Instant now = Instant.now();

        SessaoVotacao expected = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .pautaId(pauta.getId())
                .openedAt(now)
                .closesAt(now.plus(1, ChronoUnit.MINUTES))
                .closedAt(null)
                .build();

        when(pautaRepository.findById(any())).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any())).thenReturn(expected);

        // Act
        SessaoVotacao actual = sessaoVotacaoService.abrirSessaoVotacao(pauta.getId(), null);

        // Assert
        assertEquals(expected, actual);
        verify(sessaoVotacaoRepository, atLeastOnce()).save(any(SessaoVotacao.class));
    }

    @Test
    public void testAbrirSessaoVotacaoComDuracaoInvalida() {
        // Arrange
        Integer durationInMinutes = -5;
        Pauta pauta = Pauta.builder()
                .id(UUID.randomUUID())
                .titulo("Pauta de Teste")
                .build();

        Instant now = Instant.now();

        SessaoVotacao expected = SessaoVotacao.builder()
                .id(UUID.randomUUID())
                .pautaId(pauta.getId())
                .openedAt(now)
                .closesAt(now.plus(1, ChronoUnit.MINUTES))
                .closedAt(null)
                .build();

        when(pautaRepository.findById(any())).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any())).thenReturn(expected);

        // Act
        SessaoVotacao actual = sessaoVotacaoService.abrirSessaoVotacao(pauta.getId(), durationInMinutes);

        // Assert
        assertEquals(expected, actual);
        verify(sessaoVotacaoRepository, atLeastOnce()).save(any(SessaoVotacao.class));
    }

    @Test
    public void testAbrirSessaoVotacaoPautaNaoEncontrada() {
        // Arrange
        UUID pautaId = UUID.randomUUID();
        when(pautaRepository.findById(pautaId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> sessaoVotacaoService.abrirSessaoVotacao(pautaId, 5)
        );
        assertEquals("Pauta não encontrada com ID: " + pautaId, exception.getMessage());
    }

    @Test
    public void testAbrirSessaoVotacaoSessaoExistente() {
        // Arrange
        UUID pautaId = UUID.randomUUID();
        Pauta pauta = Pauta.builder()
                .id(pautaId)
                .titulo("Pauta de Teste")
                .build();

        when(pautaRepository.findById(pautaId)).thenReturn(Optional.of(pauta));
        when(sessaoVotacaoRepository.save(any())).thenThrow(new DataIntegrityViolationException("Sessão já existe"));

        // Act & Assert
        IllegalStateException exception = assertThrows(
            IllegalStateException.class,
            () -> sessaoVotacaoService.abrirSessaoVotacao(pautaId, 5)
        );
        assertEquals("Já existe uma sessão para essa pauta.", exception.getMessage());
    }
}