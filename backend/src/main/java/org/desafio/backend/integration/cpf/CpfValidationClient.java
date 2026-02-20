package org.desafio.backend.integration.cpf;

import org.desafio.backend.integration.dto.UserVoteStatusResponse;

public interface CpfValidationClient {
    UserVoteStatusResponse checkCpf(String cpf);
}
