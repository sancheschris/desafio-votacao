ALTER TABLE votacao.voto
ADD CONSTRAINT fk_voto_pauta FOREIGN KEY (pauta_id) REFERENCES votacao.pauta(id) ON DELETE CASCADE;