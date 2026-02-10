package org.desafio.backend.service;

import java.time.Instant;
import java.util.UUID;
import org.desafio.backend.domain.Pauta;
import org.desafio.backend.repository.PautaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeastOnce;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PautaServiceTest {

    @Mock
    private PautaRepository pautaRepository;
    @InjectMocks
    private PautaService pautaService;

    @Test
    void testCreatePauta__returnsSavedPauta() {
        // Arrange
        Pauta pauta = Pauta.builder()
                .id(UUID.fromString("123e4567-e89b-12d3-a456-426614174000"))
                .titulo("Aprovar o orçamento de 2026")
                .createdAt(Instant.from(Instant.parse("2026-02-10T10:00:00Z")))
                .build();

        when(pautaRepository.save(any(Pauta.class))).thenReturn(pauta);

        // Act
        Pauta createdPauta = pautaService.createPauta(pauta);

        // Assert
        assertNotNull(createdPauta);
        assertEquals(pauta.getTitulo(), createdPauta.getTitulo());
        assertEquals(pauta.getId(), createdPauta.getId());
        assertEquals(pauta.getCreatedAt(), createdPauta.getCreatedAt());
        verify(pautaRepository, atLeastOnce()).save(any(Pauta.class));
    }
}