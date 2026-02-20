package org.desafio.backend.integration.cpf;

import lombok.extern.slf4j.Slf4j;
import org.desafio.backend.exception.CpfInvalidoException;
import org.desafio.backend.integration.dto.UserVoteStatusResponse;
import org.desafio.backend.util.CpfValidator;
import java.util.Random;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class FakeCpfValidationClient implements CpfValidationClient {
    private static final Random RANDOM = new Random();

    @Override
    public UserVoteStatusResponse checkCpf(String cpf) {
        if (!CpfValidator.isValid(cpf)) {
            log.error("CPF inválido: {}", cpf);
            throw new CpfInvalidoException("CPF inválido.");
        }
        boolean able = RANDOM.nextBoolean();
        log.info("CPF {} é {}", cpf, able ? "válido para votar" : "inválido para votar");
        return new UserVoteStatusResponse(
                able ? "ABLE_TO_VOTE" : "UNABLE_TO_VOTE"
        );
    }
}
