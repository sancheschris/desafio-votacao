package org.desafio.backend.exception;

public class UsuarioNaoPodeVotarException extends RuntimeException {
    public UsuarioNaoPodeVotarException(String message) {
        super(message);
    }
}
