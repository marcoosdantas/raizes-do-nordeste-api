package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.application.dto.request.EstoqueEntradaRequest;
import com.raizesdonordeste.api.application.dto.request.EstoqueSaidaRequest;
import com.raizesdonordeste.api.application.dto.response.EstoqueItemResponse;
import com.raizesdonordeste.api.domain.exception.EstoqueInsuficienteException;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.model.EstoqueItem;
import com.raizesdonordeste.api.infrastructure.repository.EstoqueItemRepository;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.api.infrastructure.repository.UnidadeRepository;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class EstoqueService {

  private final EstoqueItemRepository estoqueItemRepository;
  private final UnidadeRepository unidadeRepository;
  private final ProdutoRepository produtoRepository;

  public List<EstoqueItemResponse> listar(String unidadeId) {
    List<EstoqueItem> itens = StringUtils.hasText(unidadeId)
        ? estoqueItemRepository.findByUnidadeId(unidadeId)
        : estoqueItemRepository.findAll();

    return itens.stream()
        .map(this::toResponse)
        .toList();
  }

  public EstoqueItemResponse entrada(EstoqueEntradaRequest request) {
    validarEntidadesRelacionadas(request.unidadeId(), request.produtoId());

    EstoqueItem estoqueItem = estoqueItemRepository
        .findByUnidadeIdAndProdutoId(request.unidadeId(), request.produtoId())
        .orElseGet(() -> EstoqueItem.builder()
            .unidadeId(request.unidadeId())
            .produtoId(request.produtoId())
            .quantidadeDisponivel(0)
            .quantidadeMinima(0)
            .build());

    estoqueItem.setQuantidadeDisponivel(estoqueItem.getQuantidadeDisponivel() + request.quantidade());
    estoqueItem.setUpdatedAt(LocalDateTime.now());

    EstoqueItem salvo = estoqueItemRepository.save(estoqueItem);
    return toResponse(salvo);
  }

  public EstoqueItemResponse saida(EstoqueSaidaRequest request) {
    validarEntidadesRelacionadas(request.unidadeId(), request.produtoId());

    EstoqueItem estoqueItem = estoqueItemRepository
        .findByUnidadeIdAndProdutoId(request.unidadeId(), request.produtoId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Item de estoque não encontrado para unidade e produto."));

    if (estoqueItem.getQuantidadeDisponivel() < request.quantidade()) {
      throw new EstoqueInsuficienteException("Quantidade insuficiente em estoque para esta operação.");
    }

    estoqueItem.setQuantidadeDisponivel(estoqueItem.getQuantidadeDisponivel() - request.quantidade());
    estoqueItem.setUpdatedAt(LocalDateTime.now());

    EstoqueItem salvo = estoqueItemRepository.save(estoqueItem);
    return toResponse(salvo);
  }

  private void validarEntidadesRelacionadas(String unidadeId, String produtoId) {
    if (!unidadeRepository.existsById(unidadeId)) {
      throw new RecursoNaoEncontradoException("Unidade não encontrada para o id informado.");
    }
    if (!produtoRepository.existsById(produtoId)) {
      throw new RecursoNaoEncontradoException("Produto não encontrado para o id informado.");
    }
  }

  private EstoqueItemResponse toResponse(EstoqueItem estoqueItem) {
    return new EstoqueItemResponse(
        estoqueItem.getId(),
        estoqueItem.getUnidadeId(),
        estoqueItem.getProdutoId(),
        estoqueItem.getQuantidadeDisponivel(),
        estoqueItem.getQuantidadeMinima(),
        estoqueItem.getUpdatedAt()
    );
  }
}

