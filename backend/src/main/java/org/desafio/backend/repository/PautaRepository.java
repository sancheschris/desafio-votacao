package org.desafio.backend.repository;

import java.util.UUID;
import org.desafio.backend.domain.Pauta;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PautaRepository extends JpaRepository<Pauta, UUID> {
}
