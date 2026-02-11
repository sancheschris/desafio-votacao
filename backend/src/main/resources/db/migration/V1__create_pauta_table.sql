CREATE TABLE votacao.pauta (
       id UUID PRIMARY KEY DEFAULT gen_random_uuid(),
       titulo VARCHAR(255) NOT NULL,
       created_at TIMESTAMPTZ NOT NULL DEFAULT now()
);



