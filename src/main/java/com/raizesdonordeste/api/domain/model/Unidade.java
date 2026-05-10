package com.raizesdonordeste.api.domain.model;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("unidades")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Unidade {

  @Id
  private String id;
  private String nome;
  private String cidade;
  private String estado;
  private boolean ativa;
  private FormatoOperacao formato;
  private String horarioAbertura;
  private String horarioFechamento;
}
