package com.raizesdonordeste.api.infrastructure.seed;

import com.raizesdonordeste.api.domain.enums.FormatoOperacao;
import com.raizesdonordeste.api.domain.enums.PerfilUsuario;
import com.raizesdonordeste.api.domain.model.EstoqueItem;
import com.raizesdonordeste.api.domain.model.Produto;
import com.raizesdonordeste.api.domain.model.Unidade;
import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.infrastructure.repository.EstoqueItemRepository;
import com.raizesdonordeste.api.infrastructure.repository.ProdutoRepository;
import com.raizesdonordeste.api.infrastructure.repository.UnidadeRepository;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataSeeder implements CommandLineRunner {

  private final UsuarioRepository usuarioRepository;
  private final UnidadeRepository unidadeRepository;
  private final ProdutoRepository produtoRepository;
  private final EstoqueItemRepository estoqueItemRepository;
  private final PasswordEncoder passwordEncoder;

  @Override
  public void run(String... args) {
    if (usuarioRepository.count() > 0) {
      log.info("Seed ignorado: coleção de usuários já possui dados.");
      return;
    }

    log.info("Iniciando seed inicial do sistema...");

    seedUsuarios();
    List<Unidade> unidades = seedUnidades();
    List<Produto> produtos = seedProdutos();
    seedEstoque(unidades, produtos);

    log.info("Seed inicial concluído com sucesso.");
  }

  private void seedUsuarios() {
    LocalDateTime now = LocalDateTime.now();

    Usuario admin = Usuario.builder()
        .nome("Administrador")
        .email("admin@raizes.com")
        .senhaHash(passwordEncoder.encode("Admin@123"))
        .perfil(PerfilUsuario.ADMIN)
        .ativo(true)
        .consentimentoLGPD(true)
        .dataConsentimento(now)
        .createdAt(now)
        .build();

    Usuario gerente = Usuario.builder()
        .nome("Gerente")
        .email("gerente@raizes.com")
        .senhaHash(passwordEncoder.encode("Gerente@123"))
        .perfil(PerfilUsuario.GERENTE)
        .ativo(true)
        .consentimentoLGPD(true)
        .dataConsentimento(now)
        .createdAt(now)
        .build();

    Usuario cliente = Usuario.builder()
        .nome("Cliente")
        .email("cliente@raizes.com")
        .senhaHash(passwordEncoder.encode("Cliente@123"))
        .perfil(PerfilUsuario.CLIENTE)
        .ativo(true)
        .consentimentoLGPD(true)
        .dataConsentimento(now)
        .createdAt(now)
        .build();

    usuarioRepository.saveAll(List.of(admin, gerente, cliente));
  }

  private List<Unidade> seedUnidades() {
    Unidade recife = Unidade.builder()
        .nome("Raízes Recife Centro")
        .cidade("Recife")
        .estado("PE")
        .ativa(true)
        .formato(FormatoOperacao.COMPLETO)
        .horarioAbertura("07:00")
        .horarioFechamento("22:00")
        .build();

    Unidade fortaleza = Unidade.builder()
        .nome("Raízes Fortaleza Aldeota")
        .cidade("Fortaleza")
        .estado("CE")
        .ativa(true)
        .formato(FormatoOperacao.COMPLETO)
        .horarioAbertura("07:00")
        .horarioFechamento("22:00")
        .build();

    return unidadeRepository.saveAll(List.of(recife, fortaleza));
  }

  private List<Produto> seedProdutos() {
    Produto tapioca = Produto.builder()
        .nome("Tapioca de Frango")
        .descricao("Tapioca recheada com frango temperado.")
        .categoria("TAPIOCA")
        .preco(new BigDecimal("14.90"))
        .disponivel(true)
        .sazonal(false)
        .periodoDisponivel(null)
        .build();

    Produto cuscuz = Produto.builder()
        .nome("Cuscuz Recheado")
        .descricao("Cuscuz nordestino com recheio especial da casa.")
        .categoria("CUSCUZ")
        .preco(new BigDecimal("12.90"))
        .disponivel(true)
        .sazonal(false)
        .periodoDisponivel(null)
        .build();

    Produto bolo = Produto.builder()
        .nome("Bolo de Macaxeira")
        .descricao("Fatia de bolo de macaxeira artesanal.")
        .categoria("BOLO")
        .preco(new BigDecimal("8.90"))
        .disponivel(true)
        .sazonal(false)
        .periodoDisponivel(null)
        .build();

    Produto suco = Produto.builder()
        .nome("Suco de Umbu")
        .descricao("Suco natural de umbu.")
        .categoria("BEBIDA")
        .preco(new BigDecimal("9.90"))
        .disponivel(true)
        .sazonal(false)
        .periodoDisponivel(null)
        .build();

    Produto cafe = Produto.builder()
        .nome("Café Nordestino")
        .descricao("Café coado tradicional.")
        .categoria("BEBIDA")
        .preco(new BigDecimal("6.90"))
        .disponivel(true)
        .sazonal(false)
        .periodoDisponivel(null)
        .build();

    Produto combo = Produto.builder()
        .nome("Combo Café da Manhã")
        .descricao("Combo especial com café e acompanhamentos.")
        .categoria("COMBO")
        .preco(new BigDecimal("24.90"))
        .disponivel(true)
        .sazonal(false)
        .periodoDisponivel(null)
        .build();

    return produtoRepository.saveAll(List.of(tapioca, cuscuz, bolo, suco, cafe, combo));
  }

  private void seedEstoque(List<Unidade> unidades, List<Produto> produtos) {
    LocalDateTime now = LocalDateTime.now();

    List<EstoqueItem> itens = unidades.stream()
        .flatMap(unidade -> produtos.stream().map(produto -> EstoqueItem.builder()
            .unidadeId(unidade.getId())
            .produtoId(produto.getId())
            .quantidadeDisponivel(50)
            .quantidadeMinima(5)
            .updatedAt(now)
            .build()))
        .toList();

    estoqueItemRepository.saveAll(itens);
  }
}

