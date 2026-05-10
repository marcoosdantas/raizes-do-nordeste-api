package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.model.Pagamento;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PagamentoRepository extends MongoRepository<Pagamento, String> {

  Optional<Pagamento> findTopByPedidoIdOrderByCreatedAtDesc(String pedidoId);
}

