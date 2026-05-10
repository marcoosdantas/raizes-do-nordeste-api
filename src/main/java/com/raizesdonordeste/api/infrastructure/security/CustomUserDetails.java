package com.raizesdonordeste.api.infrastructure.security;

import com.raizesdonordeste.api.domain.model.Usuario;
import java.util.Collection;
import java.util.List;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Getter
@RequiredArgsConstructor
public class CustomUserDetails implements UserDetails {

  private final Usuario usuario;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(new SimpleGrantedAuthority("ROLE_" + usuario.getPerfil().name()));
  }

  @Override
  public String getPassword() {
    return usuario.getSenhaHash();
  }

  @Override
  public String getUsername() {
    return usuario.getEmail();
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return usuario.isAtivo();
  }

  public String getUsuarioId() {
    return usuario.getId();
  }

  public String getPerfil() {
    return usuario.getPerfil().name();
  }
}

