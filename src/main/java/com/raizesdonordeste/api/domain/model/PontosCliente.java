package com.raizesdonordeste.api.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("pontos_clientes")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PontosCliente {

  @Id
  private String id;
  private String clienteId;
  private int saldoPontos;
  private int totalAcumulado;
  private LocalDateTime updatedAt;
}
