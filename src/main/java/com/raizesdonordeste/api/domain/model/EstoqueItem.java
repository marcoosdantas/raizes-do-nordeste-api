package com.raizesdonordeste.api.domain.model;

import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("estoque")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstoqueItem {

  @Id
  private String id;
  private String unidadeId;
  private String produtoId;
  private int quantidadeDisponivel;
  private int quantidadeMinima;
  private LocalDateTime updatedAt;
}
