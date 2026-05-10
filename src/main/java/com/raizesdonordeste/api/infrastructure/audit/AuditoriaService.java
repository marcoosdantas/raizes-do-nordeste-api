package com.raizesdonordeste.api.infrastructure.audit;

import com.raizesdonordeste.api.domain.model.HistoricoAuditoria;
import com.raizesdonordeste.api.infrastructure.repository.HistoricoAuditoriaRepository;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuditoriaService {

  private final HistoricoAuditoriaRepository historicoAuditoriaRepository;

  public HistoricoAuditoria registrarAcao(String acao, String entidade, String entidadeId, String usuarioId,
                                          String detalhe) {
    HistoricoAuditoria historicoAuditoria = HistoricoAuditoria.builder()
        .acao(acao)
        .entidade(entidade)
        .entidadeId(entidadeId)
        .usuarioId(usuarioId)
        .detalhe(detalhe)
        .timestamp(LocalDateTime.now())
        .build();

    return historicoAuditoriaRepository.save(historicoAuditoria);
  }
}

