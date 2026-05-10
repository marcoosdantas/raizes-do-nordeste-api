package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.application.dto.request.FidelidadeResgatarRequest;
import com.raizesdonordeste.api.application.dto.response.FidelidadeHistoricoResponse;
import com.raizesdonordeste.api.application.dto.response.FidelidadeSaldoResponse;
import com.raizesdonordeste.api.application.dto.response.HistoricoAuditoriaResponse;
import com.raizesdonordeste.api.application.dto.response.MensagemResponse;
import com.raizesdonordeste.api.domain.exception.ConsentimentoLGPDException;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.model.PontosCliente;
import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.infrastructure.audit.AuditoriaService;
import com.raizesdonordeste.api.infrastructure.repository.HistoricoAuditoriaRepository;
import com.raizesdonordeste.api.infrastructure.repository.PontosClienteRepository;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;
import com.raizesdonordeste.api.infrastructure.security.CustomUserDetails;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FidelidadeService {

  private final PontosClienteRepository pontosClienteRepository;
  private final HistoricoAuditoriaRepository historicoAuditoriaRepository;
  private final UsuarioRepository usuarioRepository;
  private final AuditoriaService auditoriaService;

  public FidelidadeSaldoResponse consultarSaldo(CustomUserDetails usuarioLogado) {
    Usuario usuario = validarConsentimento(usuarioLogado.getUsuarioId());

    PontosCliente pontosCliente = pontosClienteRepository.findByClienteId(usuario.getId())
        .orElseGet(() -> PontosCliente.builder()
            .clienteId(usuario.getId())
            .saldoPontos(0)
            .totalAcumulado(0)
            .updatedAt(LocalDateTime.now())
            .build());

    return new FidelidadeSaldoResponse(
        pontosCliente.getClienteId(),
        pontosCliente.getSaldoPontos(),
        pontosCliente.getTotalAcumulado(),
        pontosCliente.getUpdatedAt()
    );
  }

  public FidelidadeHistoricoResponse consultarHistorico(CustomUserDetails usuarioLogado) {
    validarConsentimento(usuarioLogado.getUsuarioId());

    return new FidelidadeHistoricoResponse(
        historicoAuditoriaRepository.findByUsuarioIdOrderByTimestampDesc(usuarioLogado.getUsuarioId())
            .stream()
            .map(item -> new HistoricoAuditoriaResponse(
                item.getId(),
                item.getAcao(),
                item.getEntidade(),
                item.getEntidadeId(),
                item.getUsuarioId(),
                item.getDetalhe(),
                item.getTimestamp()
            ))
            .toList()
    );
  }

  public MensagemResponse resgatar(FidelidadeResgatarRequest request, CustomUserDetails usuarioLogado) {
    Usuario usuario = validarConsentimento(usuarioLogado.getUsuarioId());

    if (request.pontos() % 100 != 0) {
      throw new IllegalArgumentException("O resgate deve ser em múltiplos de 100 pontos.");
    }

    PontosCliente pontosCliente = pontosClienteRepository.findByClienteId(usuario.getId())
        .orElseThrow(() -> new IllegalArgumentException("Cliente não possui pontos disponíveis para resgate."));

    if (pontosCliente.getSaldoPontos() < request.pontos()) {
      throw new IllegalArgumentException("Saldo de pontos insuficiente para o resgate.");
    }

    pontosCliente.setSaldoPontos(pontosCliente.getSaldoPontos() - request.pontos());
    pontosCliente.setUpdatedAt(LocalDateTime.now());
    pontosClienteRepository.save(pontosCliente);

    int descontoReais = (request.pontos() / 100) * 5;

    auditoriaService.registrarAcao(
        "RESGATE_FIDELIDADE",
        "PontosCliente",
        pontosCliente.getId(),
        usuario.getId(),
        "Resgate de " + request.pontos() + " pontos = R$ " + descontoReais + ",00"
    );

    return new MensagemResponse("Resgate realizado com sucesso. Desconto gerado: R$ " + descontoReais + ",00.");
  }

  private Usuario validarConsentimento(String usuarioId) {
    Usuario usuario = usuarioRepository.findById(usuarioId)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Usuário não encontrado para o id informado."));

    if (!usuario.isConsentimentoLGPD()) {
      throw new ConsentimentoLGPDException("É necessário consentimento LGPD para participar da fidelidade.");
    }

    return usuario;
  }
}

