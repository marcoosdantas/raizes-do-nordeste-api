package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.model.PontosCliente;
import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PontosClienteRepository extends MongoRepository<PontosCliente, String> {

  Optional<PontosCliente> findByClienteId(String clienteId);
}

