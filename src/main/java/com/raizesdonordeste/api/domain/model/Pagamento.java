package com.raizesdonordeste.api.domain.model;

import com.raizesdonordeste.api.domain.enums.StatusPagamento;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("pagamentos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Pagamento {

  @Id
  private String id;
  private String pedidoId;
  private BigDecimal valorTotal;
  private StatusPagamento status;
  private String provedorMock;
  private Map<String, Object> payload;
  private LocalDateTime createdAt;
}
