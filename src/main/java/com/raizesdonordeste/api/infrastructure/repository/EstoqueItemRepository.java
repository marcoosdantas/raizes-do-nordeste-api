package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.model.EstoqueItem;
import java.util.List;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EstoqueItemRepository extends MongoRepository<EstoqueItem, String> {

  List<EstoqueItem> findByUnidadeId(String unidadeId);

  List<EstoqueItem> findByUnidadeIdAndQuantidadeDisponivelGreaterThan(String unidadeId, int quantidadeDisponivel);

  Optional<EstoqueItem> findByUnidadeIdAndProdutoId(String unidadeId, String produtoId);
}
