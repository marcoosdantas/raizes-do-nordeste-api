package com.raizesdonordeste.api.infrastructure.repository;

import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import com.raizesdonordeste.api.domain.model.Pedido;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface PedidoRepository extends MongoRepository<Pedido, String> {

  Optional<Pedido> findByIdAndClienteId(String id, String clienteId);

  Page<Pedido> findByClienteId(String clienteId, Pageable pageable);

  Page<Pedido> findByUnidadeId(String unidadeId, Pageable pageable);

  Page<Pedido> findByCanalPedido(CanalPedido canalPedido, Pageable pageable);

  Page<Pedido> findByStatus(StatusPedido status, Pageable pageable);

  Page<Pedido> findByCanalPedidoAndStatus(CanalPedido canalPedido, StatusPedido status, Pageable pageable);

  Page<Pedido> findByCanalPedidoAndUnidadeId(CanalPedido canalPedido, String unidadeId, Pageable pageable);

  Page<Pedido> findByStatusAndUnidadeId(StatusPedido status, String unidadeId, Pageable pageable);

  Page<Pedido> findByCanalPedidoAndStatusAndUnidadeId(CanalPedido canalPedido, StatusPedido status, String unidadeId,
                                                      Pageable pageable);
}
