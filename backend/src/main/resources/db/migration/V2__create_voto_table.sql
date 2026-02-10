CREATE TABLE IF NOT EXISTS votacao.voto (
    id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
    pauta_id UUID NOT NULL,
    associado_id VARCHAR(100) NOT NULL,
    valor VARCHAR(10) NOT NULL,
    created_at TIMESTAMPTZ NOT NULL DEFAULT now(),
    CONSTRAINT ck_voto_valor CHECK (valor IN ('SIM', 'NAO'))
);

-- 1 voto por associado por pauta
CREATE UNIQUE INDEX IF NOT EXISTS uk_voto_pauta_associado
    ON votacao.voto (pauta_id, associado_id);