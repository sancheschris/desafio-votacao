package org.desafio.backend.service;

import java.util.Optional;
import java.util.UUID;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.dto.ResultadoVotacaoResponse;
import org.desafio.backend.exception.ResourceNotFoundException;
import org.desafio.backend.repository.PautaRepository;
import org.desafio.backend.repository.VotoRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ResultadoVotacaoServiceTest {

    @InjectMocks
    private ResultadoVotacaoService resultadoVotacaoService;
    @Mock
    private PautaRepository pautaRepository;
    @Mock
    private VotoRepository votoRepository;

    @Test
    void testResultado__when_total_votos_is_sim() {
        // Arrange
        Pauta pauta = Pauta.builder()
                .id(java.util.UUID.randomUUID())
                .titulo("Pauta de Teste")
                .build();

        ResultadoVotacaoResponse expected = new ResultadoVotacaoResponse(pauta.getId(), 15L, 10L, VotoValor.SIM.name());

        when(pautaRepository.findById(any())).thenReturn(Optional.of(pauta));
        when(votoRepository.countByPautaIdAndValor(any(), any())).thenReturn(15L, 10L);

        // Act
        ResultadoVotacaoResponse actual = resultadoVotacaoService.resultado(pauta.getId());

        // Assert
        assertEquals(expected, actual);
        assertEquals(VotoValor.SIM, VotoValor.valueOf(actual.resultado()));
    }

    @Test
    void testResultado__when_total_votos_is_nao() {
        // Arrange
        Pauta pauta = Pauta.builder()
                .id(java.util.UUID.randomUUID())
                .titulo("Pauta de Teste")
                .build();

        ResultadoVotacaoResponse expected = new ResultadoVotacaoResponse(pauta.getId(), 10L, 15L, VotoValor.NAO.name());

        when(pautaRepository.findById(any())).thenReturn(Optional.of(pauta));
        when(votoRepository.countByPautaIdAndValor(any(), any())).thenReturn(10L, 15L);

        // Act
        ResultadoVotacaoResponse actual = resultadoVotacaoService.resultado(pauta.getId());

        // Assert
        assertEquals(expected, actual);
        assertEquals(VotoValor.NAO, VotoValor.valueOf(actual.resultado()));
    }

    @Test
    void testResultado__when_total_votos_is_empate() {
        // Arrange
        Pauta pauta = Pauta.builder()
                .id(java.util.UUID.randomUUID())
                .titulo("Pauta de Teste")
                .build();

        ResultadoVotacaoResponse expected = new ResultadoVotacaoResponse(pauta.getId(), 10L, 10L, "EMPATE");

        when(pautaRepository.findById(any())).thenReturn(Optional.of(pauta));
        when(votoRepository.countByPautaIdAndValor(any(), any())).thenReturn(10L, 10L);

        // Act
        ResultadoVotacaoResponse actual = resultadoVotacaoService.resultado(pauta.getId());

        // Assert
        assertEquals(expected, actual);
        assertEquals("EMPATE", actual.resultado());
    }

    @Test
    void testResultado__when_pauta_not_found() {
        // Arrange
        when(pautaRepository.findById(any())).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, ()
                -> resultadoVotacaoService.resultado(UUID.randomUUID()));

        assertTrue(exception.getMessage().contains("Pauta não encontrada"));
    }

}