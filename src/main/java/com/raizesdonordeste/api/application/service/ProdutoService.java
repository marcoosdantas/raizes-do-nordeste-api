package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.application.dto.request.ProdutoCreateRequest;
import com.raizesdonordeste.api.application.dto.request.ProdutoUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.PagedResponse;
import com.raizesdonordeste.api.application.dto.response.ProdutoResponse;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.model.Produto;
import com.raizesdonordeste.api.infrastructure.repository.EstoqueItemRepository;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class ProdutoService {

  private final ProdutoRepository produtoRepository;
  private final EstoqueItemRepository estoqueItemRepository;

  public PagedResponse<ProdutoResponse> listarPublico(String categoria, String unidadeId, int page, int limit) {
    int safePage = Math.max(page, 0);
    int safeLimit = Math.max(limit, 1);
    Pageable pageable = PageRequest.of(safePage, safeLimit, Sort.by("nome").ascending());

    Page<Produto> produtosPage;
    boolean temCategoria = StringUtils.hasText(categoria);
    boolean temUnidade = StringUtils.hasText(unidadeId);

    if (!temUnidade) {
      produtosPage = temCategoria
          ? produtoRepository.findByCategoriaIgnoreCaseAndDisponivelTrue(categoria, pageable)
          : produtoRepository.findByDisponivelTrue(pageable);
    } else {
      List<String> produtoIds = estoqueItemRepository.findByUnidadeIdAndQuantidadeDisponivelGreaterThan(unidadeId, 0)
          .stream()
          .map(item -> item.getProdutoId())
          .distinct()
          .toList();

      if (produtoIds.isEmpty()) {
        return new PagedResponse<>(List.of(), safePage, safeLimit, 0L, 0);
      }

      produtosPage = temCategoria
          ? produtoRepository.findByIdInAndCategoriaIgnoreCaseAndDisponivelTrue(produtoIds, categoria, pageable)
          : produtoRepository.findByIdInAndDisponivelTrue(produtoIds, pageable);
    }

    List<ProdutoResponse> content = produtosPage.getContent().stream()
        .map(this::toResponse)
        .toList();

    return new PagedResponse<>(
        content,
        safePage,
        safeLimit,
        produtosPage.getTotalElements(),
        produtosPage.getTotalPages()
    );
  }

  public ProdutoResponse buscarPorId(String id) {
    Produto produto = produtoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado para o id informado."));

    return toResponse(produto);
  }

  public ProdutoResponse criar(ProdutoCreateRequest request) {
    Produto produto = Produto.builder()
        .nome(request.nome())
        .descricao(request.descricao())
        .categoria(request.categoria())
        .preco(request.preco())
        .disponivel(request.disponivel())
        .sazonal(request.sazonal())
        .periodoDisponivel(request.periodoDisponivel())
        .build();

    Produto salvo = produtoRepository.save(produto);
    return toResponse(salvo);
  }

  public ProdutoResponse atualizar(String id, ProdutoUpdateRequest request) {
    Produto produto = produtoRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado para o id informado."));

    if (request.nome() != null) {
      produto.setNome(request.nome());
    }
    if (request.descricao() != null) {
      produto.setDescricao(request.descricao());
    }
    if (request.categoria() != null) {
      produto.setCategoria(request.categoria());
    }
    if (request.preco() != null) {
      produto.setPreco(request.preco());
    }
    if (request.disponivel() != null) {
      produto.setDisponivel(request.disponivel());
    }
    if (request.sazonal() != null) {
      produto.setSazonal(request.sazonal());
    }
    if (request.periodoDisponivel() != null) {
      produto.setPeriodoDisponivel(request.periodoDisponivel());
    }

    Produto atualizado = produtoRepository.save(produto);
    return toResponse(atualizado);
  }

  private ProdutoResponse toResponse(Produto produto) {
    return new ProdutoResponse(
        produto.getId(),
        produto.getNome(),
        produto.getDescricao(),
        produto.getCategoria(),
        produto.getPreco(),
        produto.isDisponivel(),
        produto.isSazonal(),
        produto.getPeriodoDisponivel()
    );
  }
}

