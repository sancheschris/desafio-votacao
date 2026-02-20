package org.desafio.backend.service;

import java.time.Instant;
import java.util.UUID;
import lombok.extern.slf4j.Slf4j;
import org.desafio.backend.domain.SessaoVotacao;
import org.desafio.backend.domain.Voto;
import org.desafio.backend.domain.VotoValor;
import org.desafio.backend.exception.AssociadoJaVotouException;
import org.desafio.backend.exception.ResourceNotFoundException;
import org.desafio.backend.exception.SessaoEncerradaException;
import org.desafio.backend.exception.UsuarioNaoPodeVotarException;
import org.desafio.backend.integration.cpf.FakeCpfValidationClient;
import org.desafio.backend.integration.cpf.StatusCpf;
import org.desafio.backend.integration.dto.UserVoteStatusResponse;
import org.desafio.backend.repository.SessaoVotacaoRepository;
import org.desafio.backend.repository.VotoRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static java.time.Instant.now;

@Service
@Slf4j
public class VotoService {

    private final VotoRepository votoRepository;
    private final SessaoVotacaoRepository sessaoVotacaoRepository;
    private final FakeCpfValidationClient  cpfValidationClient;

    public VotoService(VotoRepository votoRepository,
                       SessaoVotacaoRepository sessaoVotacaoRepository,
                       FakeCpfValidationClient cpfValidationClient) {
        this.votoRepository = votoRepository;
        this.sessaoVotacaoRepository = sessaoVotacaoRepository;
        this.cpfValidationClient = cpfValidationClient;
    }

    @Transactional
    public Voto votar(UUID pautaId, String associadoId, VotoValor valor) {
        log.info("Validando CPF: {}", associadoId);
        UserVoteStatusResponse statusCpf = cpfValidationClient.checkCpf(associadoId);

        if (StatusCpf.UNABLE_TO_VOTE.name().equals(statusCpf.status())) {
            throw new UsuarioNaoPodeVotarException("Usuário não pode votar.");
        }
        log.info("Registrando voto para pauta ID: {}, associado ID: {}, valor: {}", pautaId, associadoId, valor);

        SessaoVotacao sessao = sessaoVotacaoRepository.findByPautaId(pautaId)
                .orElseThrow(() -> {
                    log.warn("Sessão de votação não encontrada para pauta ID: {}", pautaId);
                    return new ResourceNotFoundException("Sessão de votação não encontrada para esta pauta.");
                });

        Instant now = now();

        if (sessao.getClosedAt() != null || !now.isBefore(sessao.getClosesAt())) {
            log.warn("Sessão de votação encerrada para pauta ID: {}", pautaId);
            throw new SessaoEncerradaException("A sessão de votação para esta pauta já está encerrada.");
        }

        if (votoRepository.existsByPautaIdAndAssociadoId(pautaId, associadoId)) {
            log.warn("Associado ID: {} já votou na pauta ID: {}", associadoId, pautaId);
            throw new AssociadoJaVotouException("Associado já votou nesta pauta.");
        }

        Voto voto = Voto.builder()
                .pautaId(pautaId)
                .associadoId(associadoId)
                .valor(valor)
                .createdAt(now)
                .build();

        try {
            Voto savedVoto = votoRepository.save(voto);
            log.info("Voto registrado com ID: {} para pauta ID: {}, associado ID: {}", savedVoto.getId(), pautaId, associadoId);
            return savedVoto;
        } catch (DataIntegrityViolationException ex) {
            log.error("Erro ao registrar voto para pauta ID: {}, associado ID: {}: {}", pautaId, associadoId, ex.getMessage(), ex);
            throw new AssociadoJaVotouException("Associado já votou nesta pauta");
        }
    }
}
