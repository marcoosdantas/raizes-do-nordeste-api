package com.raizesdonordeste.api.domain.model;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("pedidos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pedido {

  @Id
  private String id;
  private String clienteId;
  private String unidadeId;
  private CanalPedido canalPedido;
  private List<ItemPedido> itens;
  private StatusPedido status;
  private BigDecimal total;
  private String formaPagamento;
  private LocalDateTime createdAt;
  private LocalDateTime updatedAt;
}
