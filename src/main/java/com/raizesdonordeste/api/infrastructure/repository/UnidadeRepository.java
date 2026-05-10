package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.model.Unidade;
import java.util.List;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UnidadeRepository extends MongoRepository<Unidade, String> {

  List<Unidade> findByAtivaTrue();
}

