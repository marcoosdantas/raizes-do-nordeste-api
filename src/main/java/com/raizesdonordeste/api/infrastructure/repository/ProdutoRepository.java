package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.model.Produto;
import java.util.Collection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface ProdutoRepository extends MongoRepository<Produto, String> {

  Page<Produto> findByCategoriaIgnoreCase(String categoria, Pageable pageable);

  Page<Produto> findByDisponivelTrue(Pageable pageable);

  Page<Produto> findByCategoriaIgnoreCaseAndDisponivelTrue(String categoria, Pageable pageable);

  Page<Produto> findByIdInAndDisponivelTrue(Collection<String> ids, Pageable pageable);

  Page<Produto> findByIdInAndCategoriaIgnoreCaseAndDisponivelTrue(Collection<String> ids, String categoria,
                                                                   Pageable pageable);
}
