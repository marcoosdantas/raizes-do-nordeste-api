package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.model.HistoricoAuditoria;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HistoricoAuditoriaRepository extends MongoRepository<HistoricoAuditoria, String> {

  List<HistoricoAuditoria> findByUsuarioIdOrderByTimestampDesc(String usuarioId);
}

