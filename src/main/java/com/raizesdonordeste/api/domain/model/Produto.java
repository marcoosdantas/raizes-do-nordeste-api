package com.raizesdonordeste.api.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("produtos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Produto {

  @Id
  private String id;
  private String nome;
  private String descricao;
  private String categoria;
  private BigDecimal preco;
  private boolean disponivel;
  private boolean sazonal;
  private String periodoDisponivel;
}
