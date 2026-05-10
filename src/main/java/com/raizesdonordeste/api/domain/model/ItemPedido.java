package com.raizesdonordeste.api.domain.model;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ItemPedido {

  private String produtoId;
  private String nomeProduto;
  private int quantidade;
  private BigDecimal precoUnitario;
  private BigDecimal subtotal;
}
