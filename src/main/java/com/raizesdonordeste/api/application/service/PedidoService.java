package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.application.dto.request.PedidoCreateRequest;
import com.raizesdonordeste.api.application.dto.request.PedidoItemRequest;
import com.raizesdonordeste.api.application.dto.request.PedidoStatusUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.MensagemResponse;
import com.raizesdonordeste.api.application.dto.response.PagedResponse;
import com.raizesdonordeste.api.application.dto.response.PedidoItemResponse;
import com.raizesdonordeste.api.application.dto.response.PedidoResponse;
import com.raizesdonordeste.api.domain.enums.CanalPedido;
import com.raizesdonordeste.api.domain.enums.StatusPedido;
import com.raizesdonordeste.api.domain.exception.EstoqueInsuficienteException;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.exception.TransicaoStatusInvalidaException;
import com.raizesdonordeste.api.domain.model.EstoqueItem;
import com.raizesdonordeste.api.domain.model.ItemPedido;
import com.raizesdonordeste.api.domain.model.Pedido;
import com.raizesdonordeste.api.domain.model.Produto;
import com.raizesdonordeste.api.domain.model.Unidade;
import com.raizesdonordeste.api.infrastructure.audit.AuditoriaService;
import com.raizesdonordeste.api.infrastructure.repository.EstoqueItemRepository;
import com.raizesdonordeste.api.infrastructure.repository.PedidoRepository;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.api.infrastructure.repository.UnidadeRepository;
import com.raizesdonordeste.api.infrastructure.security.CustomUserDetails;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

@Service
@RequiredArgsConstructor
public class PedidoService {

  private final PedidoRepository pedidoRepository;
  private final UnidadeRepository unidadeRepository;
  private final ProdutoRepository produtoRepository;
  private final EstoqueItemRepository estoqueItemRepository;
  private final AuditoriaService auditoriaService;

  public PedidoResponse criar(PedidoCreateRequest request, CustomUserDetails usuarioLogado) {
    Unidade unidade = unidadeRepository.findById(request.unidadeId())
        .filter(Unidade::isAtiva)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada ou inativa."));

    List<ItemPedido> itens = request.itens().stream()
        .map(item -> montarItemPedido(unidade.getId(), item))
        .toList();

    BigDecimal total = itens.stream()
        .map(ItemPedido::getSubtotal)
        .reduce(BigDecimal.ZERO, BigDecimal::add);

    Pedido pedido = Pedido.builder()
        .clienteId(usuarioLogado.getUsuarioId())
        .unidadeId(unidade.getId())
        .canalPedido(request.canalPedido())
        .itens(itens)
        .status(StatusPedido.AGUARDANDO_PAGAMENTO)
        .total(total)
        .formaPagamento(request.formaPagamento())
        .createdAt(LocalDateTime.now())
        .updatedAt(LocalDateTime.now())
        .build();

    Pedido salvo = pedidoRepository.save(pedido);

    auditoriaService.registrarAcao(
        "CRIAR_PEDIDO",
        "Pedido",
        salvo.getId(),
        usuarioLogado.getUsuarioId(),
        "Pedido criado com status AGUARDANDO_PAGAMENTO"
    );

    return toResponse(salvo);
  }

  public PagedResponse<PedidoResponse> listar(String canalPedido, String status, String unidadeId, int page, int limit) {
    int safePage = Math.max(page, 0);
    int safeLimit = Math.max(limit, 1);
    Pageable pageable = PageRequest.of(safePage, safeLimit, Sort.by("createdAt").descending());

    CanalPedido canalFiltro = parseCanal(canalPedido);
    StatusPedido statusFiltro = parseStatus(status);
    boolean temUnidade = StringUtils.hasText(unidadeId);

    Page<Pedido> pedidosPage = buscarComFiltros(canalFiltro, statusFiltro, unidadeId, temUnidade, pageable);

    return new PagedResponse<>(
        pedidosPage.getContent().stream().map(this::toResponse).toList(),
        safePage,
        safeLimit,
        pedidosPage.getTotalElements(),
        pedidosPage.getTotalPages()
    );
  }

  public PedidoResponse buscarPorIdComAcesso(String pedidoId, CustomUserDetails usuarioLogado) {
    Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o id informado."));

    boolean gerenteOuAdmin = isGerenteOuAdmin(usuarioLogado.getPerfil());
    boolean dono = pedido.getClienteId().equals(usuarioLogado.getUsuarioId());
    if (!gerenteOuAdmin && !dono) {
      throw new AccessDeniedException("Você não possui permissão para acessar este pedido.");
    }

    return toResponse(pedido);
  }

  public PedidoResponse atualizarStatus(String pedidoId, PedidoStatusUpdateRequest request, CustomUserDetails usuarioLogado) {
    Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o id informado."));

    StatusPedido statusAnterior = pedido.getStatus();
    validarTransicaoStatus(pedido.getStatus(), request.status(), usuarioLogado.getPerfil());

    pedido.setStatus(request.status());
    pedido.setUpdatedAt(LocalDateTime.now());
    Pedido atualizado = pedidoRepository.save(pedido);

    auditoriaService.registrarAcao(
        "ATUALIZAR_STATUS_PEDIDO",
        "Pedido",
        atualizado.getId(),
        usuarioLogado.getUsuarioId(),
        "Status alterado de " + statusAnterior + " para " + request.status()
    );

    return toResponse(atualizado);
  }

  public MensagemResponse cancelar(String pedidoId, CustomUserDetails usuarioLogado) {
    Pedido pedido = pedidoRepository.findById(pedidoId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Pedido não encontrado para o id informado."));

    pedido.setStatus(StatusPedido.CANCELADO);
    pedido.setUpdatedAt(LocalDateTime.now());
    pedidoRepository.save(pedido);

    auditoriaService.registrarAcao(
        "CANCELAR_PEDIDO",
        "Pedido",
        pedido.getId(),
        usuarioLogado.getUsuarioId(),
        "Pedido cancelado por perfil " + usuarioLogado.getPerfil()
    );

    return new MensagemResponse("Pedido cancelado com sucesso.");
  }

  private ItemPedido montarItemPedido(String unidadeId, PedidoItemRequest itemRequest) {
    Produto produto = produtoRepository.findById(itemRequest.produtoId())
        .orElseThrow(() -> new RecursoNaoEncontradoException("Produto não encontrado para o id informado."));

    EstoqueItem estoqueItem = estoqueItemRepository.findByUnidadeIdAndProdutoId(unidadeId, itemRequest.produtoId())
        .orElseThrow(() -> new EstoqueInsuficienteException("Produto sem estoque disponível para a unidade informada."));

    if (estoqueItem.getQuantidadeDisponivel() < itemRequest.quantidade()) {
      throw new EstoqueInsuficienteException("Quantidade insuficiente para o produto " + produto.getNome() + ".");
    }

    BigDecimal subtotal = produto.getPreco().multiply(BigDecimal.valueOf(itemRequest.quantidade()));

    return ItemPedido.builder()
        .produtoId(produto.getId())
        .nomeProduto(produto.getNome())
        .quantidade(itemRequest.quantidade())
        .precoUnitario(produto.getPreco())
        .subtotal(subtotal)
        .build();
  }

  private Page<Pedido> buscarComFiltros(CanalPedido canalFiltro,
                                        StatusPedido statusFiltro,
                                        String unidadeId,
                                        boolean temUnidade,
                                        Pageable pageable) {
    if (canalFiltro != null && statusFiltro != null && temUnidade) {
      return pedidoRepository.findByCanalPedidoAndStatusAndUnidadeId(canalFiltro, statusFiltro, unidadeId, pageable);
    }
    if (canalFiltro != null && statusFiltro != null) {
      return pedidoRepository.findByCanalPedidoAndStatus(canalFiltro, statusFiltro, pageable);
    }
    if (canalFiltro != null && temUnidade) {
      return pedidoRepository.findByCanalPedidoAndUnidadeId(canalFiltro, unidadeId, pageable);
    }
    if (statusFiltro != null && temUnidade) {
      return pedidoRepository.findByStatusAndUnidadeId(statusFiltro, unidadeId, pageable);
    }
    if (canalFiltro != null) {
      return pedidoRepository.findByCanalPedido(canalFiltro, pageable);
    }
    if (statusFiltro != null) {
      return pedidoRepository.findByStatus(statusFiltro, pageable);
    }
    if (temUnidade) {
      return pedidoRepository.findByUnidadeId(unidadeId, pageable);
    }
    return pedidoRepository.findAll(pageable);
  }

  private void validarTransicaoStatus(StatusPedido atual, StatusPedido novo, String perfil) {
    if (novo == StatusPedido.CANCELADO) {
      if (!isGerenteOuAdmin(perfil)) {
        throw new TransicaoStatusInvalidaException("Apenas GERENTE ou ADMIN podem cancelar pedidos.");
      }
      return;
    }

    if (atual == StatusPedido.PAGO && novo == StatusPedido.EM_PREPARO && (isCozinha(perfil) || isGerente(perfil))) {
      return;
    }

    if (atual == StatusPedido.EM_PREPARO && novo == StatusPedido.PRONTO && (isCozinha(perfil) || isGerente(perfil))) {
      return;
    }

    if (atual == StatusPedido.PRONTO && novo == StatusPedido.ENTREGUE && (isAtendente(perfil) || isGerente(perfil))) {
      return;
    }

    throw new TransicaoStatusInvalidaException("Transição de status inválida para este perfil.");
  }

  private CanalPedido parseCanal(String canalPedido) {
    if (!StringUtils.hasText(canalPedido)) {
      return null;
    }
    try {
      return CanalPedido.valueOf(canalPedido.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("canalPedido inválido.");
    }
  }

  private StatusPedido parseStatus(String status) {
    if (!StringUtils.hasText(status)) {
      return null;
    }
    try {
      return StatusPedido.valueOf(status.trim().toUpperCase());
    } catch (IllegalArgumentException ex) {
      throw new IllegalArgumentException("status inválido.");
    }
  }

  private boolean isGerenteOuAdmin(String perfil) {
    return "GERENTE".equals(perfil) || "ADMIN".equals(perfil);
  }

  private boolean isGerente(String perfil) {
    return "GERENTE".equals(perfil);
  }

  private boolean isCozinha(String perfil) {
    return "COZINHA".equals(perfil);
  }

  private boolean isAtendente(String perfil) {
    return "ATENDENTE".equals(perfil);
  }

  private PedidoResponse toResponse(Pedido pedido) {
    return new PedidoResponse(
        pedido.getId(),
        pedido.getClienteId(),
        pedido.getUnidadeId(),
        pedido.getCanalPedido(),
        pedido.getItens().stream().map(item -> new PedidoItemResponse(
            item.getProdutoId(),
            item.getNomeProduto(),
            item.getQuantidade(),
            item.getPrecoUnitario(),
            item.getSubtotal()
        )).toList(),
        pedido.getStatus(),
        pedido.getTotal(),
        pedido.getFormaPagamento(),
        pedido.getCreatedAt(),
        pedido.getUpdatedAt()
    );
  }
}
