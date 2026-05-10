package com.raizesdonordeste.api.infrastructure.security;

import com.raizesdonordeste.api.domain.model.Usuario;
import com.raizesdonordeste.api.infrastructure.repository.UsuarioRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

  private final UsuarioRepository usuarioRepository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    Usuario usuario = usuarioRepository.findByEmailIgnoreCase(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado para o email informado."));

    return new CustomUserDetails(usuario);
  }
}

