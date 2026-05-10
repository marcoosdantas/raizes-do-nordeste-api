package com.raizesdonordeste.api.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("auditoria")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HistoricoAuditoria {

  @Id
  private String id;
  private String acao;
  private String entidade;
  private String entidadeId;
  private String usuarioId;
  private String detalhe;
  private LocalDateTime timestamp;
}
