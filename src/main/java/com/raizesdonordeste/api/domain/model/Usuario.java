package com.raizesdonordeste.api.domain.model;

import com.raizesdonordeste.api.domain.enums.PerfilUsuario;
import java.time.LocalDateTime;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document("usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Usuario {

  @Id
  private String id;
  private String nome;
  private String email;
  private String senhaHash;
  private PerfilUsuario perfil;
  private boolean ativo;
  private boolean consentimentoLGPD;
  private LocalDateTime dataConsentimento;
  private LocalDateTime createdAt;
}
