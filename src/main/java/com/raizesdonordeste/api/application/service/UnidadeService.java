package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.application.dto.request.UnidadeCreateRequest;
import com.raizesdonordeste.api.application.dto.request.UnidadeUpdateRequest;
import com.raizesdonordeste.api.application.dto.response.UnidadeResponse;
import com.raizesdonordeste.api.domain.exception.RecursoNaoEncontradoException;
import com.raizesdonordeste.api.domain.model.Unidade;
import com.raizesdonordeste.api.infrastructure.repository.UnidadeRepository;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UnidadeService {

  private final UnidadeRepository unidadeRepository;

  public List<UnidadeResponse> listar() {
    return unidadeRepository.findAll().stream()
        .map(this::toResponse)
        .toList();
  }

  public UnidadeResponse buscarPorId(String id) {
    Unidade unidade = unidadeRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada para o id informado."));

    return toResponse(unidade);
  }

  public UnidadeResponse criar(UnidadeCreateRequest request) {
    Unidade unidade = Unidade.builder()
        .nome(request.nome())
        .cidade(request.cidade())
        .estado(request.estado())
        .ativa(request.ativa())
        .formato(request.formato())
        .horarioAbertura(request.horarioAbertura())
        .horarioFechamento(request.horarioFechamento())
        .build();

    Unidade salva = unidadeRepository.save(unidade);
    return toResponse(salva);
  }

  public UnidadeResponse atualizarParcial(String id, UnidadeUpdateRequest request) {
    Unidade unidade = unidadeRepository.findById(id)
        .orElseThrow(() -> new RecursoNaoEncontradoException("Unidade não encontrada para o id informado."));

    if (request.nome() != null) {
      unidade.setNome(request.nome());
    }
    if (request.cidade() != null) {
      unidade.setCidade(request.cidade());
    }
    if (request.estado() != null) {
      unidade.setEstado(request.estado());
    }
    if (request.ativa() != null) {
      unidade.setAtiva(request.ativa());
    }
    if (request.formato() != null) {
      unidade.setFormato(request.formato());
    }
    if (request.horarioAbertura() != null) {
      unidade.setHorarioAbertura(request.horarioAbertura());
    }
    if (request.horarioFechamento() != null) {
      unidade.setHorarioFechamento(request.horarioFechamento());
    }

    Unidade atualizada = unidadeRepository.save(unidade);
    return toResponse(atualizada);
  }

  private UnidadeResponse toResponse(Unidade unidade) {
    return new UnidadeResponse(
        unidade.getId(),
        unidade.getNome(),
        unidade.getCidade(),
        unidade.getEstado(),
        unidade.isAtiva(),
        unidade.getFormato(),
        unidade.getHorarioAbertura(),
        unidade.getHorarioFechamento()
    );
  }
}

