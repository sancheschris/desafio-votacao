CREATE TABLE IF NOT EXISTS votacao.sessao_votacao (
    id UUID PRIMARY KEY,
    pauta_id UUID NOT NULL,
    opened_at TIMESTAMPTZ NOT NULL,
    closes_at TIMESTAMPTZ NOT NULL,
    closed_at TIMESTAMPTZ NULL,

    CONSTRAINT fk_sessao_pauta
    FOREIGN KEY (pauta_id) REFERENCES votacao.pauta(id)
    ON DELETE CASCADE
    );

-- 1 sessão por pauta
CREATE UNIQUE INDEX IF NOT EXISTS uk_sessao_por_pauta
    ON votacao.sessao_votacao (pauta_id);