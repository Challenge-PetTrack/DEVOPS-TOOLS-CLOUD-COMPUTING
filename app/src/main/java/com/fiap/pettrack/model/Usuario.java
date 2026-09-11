package com.fiap.pettrack.model;

import com.fiap.pettrack.model.enums.PerfilEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Schema(description = "Entidade de Usuário para autenticação e autorização via Spring Security")
@Entity
@Table(name = "TB_USUARIO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Usuario implements UserDetails {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_usuario")
    @SequenceGenerator(name = "seq_usuario", sequenceName = "SEQ_USUARIO", allocationSize = 1)
    @Column(name = "ID_USUARIO")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "NM_NOME", nullable = false, length = 150)
    private String nome;

    @NotBlank(message = "O e-mail é obrigatório")
    @Email(message = "Formato de e-mail inválido")
    @Column(name = "DS_EMAIL", nullable = false, unique = true, length = 200)
    private String email;

    @NotBlank(message = "A senha é obrigatória")
    @Size(min = 6, message = "A senha deve ter no mínimo 6 caracteres")
    @Column(name = "DS_SENHA", nullable = false, length = 255)
    private String senha;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_ROLE", nullable = false, length = 50)
    private PerfilEnum role;

    @Column(name = "ST_ATIVO", length = 1, nullable = false)
    private String ativo = "S";

    @ManyToOne
    @JoinColumn(name = "ID_TUTOR")
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "ID_CLINICA")
    private Clinica clinica;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.name()));
    }

    @Override
    public String getPassword() {
        return this.senha;
    }

    @Override
    public String getUsername() {
        return this.email;
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
        return "S".equalsIgnoreCase(this.ativo);
    }
}
