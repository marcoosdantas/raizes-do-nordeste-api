package com.raizesdonordeste.api.application.service;

import com.raizesdonordeste.api.application.dto.request.AuthLoginRequest;
import com.raizesdonordeste.api.application.dto.request.AuthRegisterRequest;
import com.raizesdonordeste.api.application.dto.response.AuthResponse;
import com.raizesdonordeste.api.application.dto.response.UsuarioResumoResponse;
import com.raizesdonordeste.api.domain.enums.PerfilUsuario;
import com.raizesdonordeste.api.domain.exception.ConsentimentoLGPDException;
import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.infrastructure.audit.AuditoriaService;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;
import com.raizesdonordeste.api.infrastructure.security.JwtUtil;
import java.time.LocalDateTime;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {

  private final UsuarioRepository usuarioRepository;
  private final PasswordEncoder passwordEncoder;
  private final AuthenticationManager authenticationManager;
  private final JwtUtil jwtUtil;
  private final AuditoriaService auditoriaService;

  @Value("${app.jwt.expiration}")
  private long jwtExpirationMs;

  public AuthResponse register(AuthRegisterRequest request) {
    if (!Boolean.TRUE.equals(request.consentimentoLGPD())) {
      throw new ConsentimentoLGPDException("É obrigatório consentir com a LGPD para realizar o cadastro.");
    }

    if (usuarioRepository.existsByEmailIgnoreCase(request.email())) {
      throw new IllegalArgumentException("Já existe um usuário cadastrado com este e-mail.");
    }

    Usuario usuario = Usuario.builder()
        .nome(request.nome())
        .email(request.email())
        .senhaHash(passwordEncoder.encode(request.senha()))
        .perfil(PerfilUsuario.CLIENTE)
        .ativo(true)
        .consentimentoLGPD(true)
        .dataConsentimento(LocalDateTime.now())
        .createdAt(LocalDateTime.now())
        .build();

    Usuario salvo = usuarioRepository.save(usuario);
    String token = jwtUtil.generateToken(salvo);

    return new AuthResponse(
        token,
        "Bearer",
        jwtExpirationMs,
        toUsuarioResumoResponse(salvo)
    );
  }

  public AuthResponse login(AuthLoginRequest request) {
    authenticationManager.authenticate(
        new UsernamePasswordAuthenticationToken(request.email(), request.senha())
    );

    Usuario usuario = usuarioRepository.findByEmailIgnoreCase(request.email())
        .orElseThrow(() -> new IllegalArgumentException("Usuário não encontrado para o e-mail informado."));

    auditoriaService.registrarAcao(
        "LOGIN",
        "Usuario",
        usuario.getId(),
        usuario.getId(),
        "Login realizado com sucesso"
    );

    String token = jwtUtil.generateToken(usuario);

    return new AuthResponse(
        token,
        "Bearer",
        jwtExpirationMs,
        toUsuarioResumoResponse(usuario)
    );
  }

  private UsuarioResumoResponse toUsuarioResumoResponse(Usuario usuario) {
    return new UsuarioResumoResponse(
        usuario.getId(),
        usuario.getNome(),
        usuario.getEmail(),
        usuario.getPerfil()
    );
  }
}
