package com.fiap.pettrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Entidade que representa a tabela TB_TUTOR no Oracle DB.")
@Entity
@Table(name = "TB_TUTOR")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Tutor {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_tutor")
    @SequenceGenerator(name = "seq_tutor", sequenceName = "SEQ_TUTOR", allocationSize = 1)
    @Column(name = "ID_TUTOR")
    @Schema(description = "Atributo de identificação do Tutor.")
    private Long id;

    @NotBlank(message = "O nome é um campo obrigatório")
    @Column(name = "NM_TUTOR", nullable = false, length = 150)
    @Schema(description = "Atributo do nome Tutor.")
    private String nome;

    @Email(message = "Informe um email válido")
    @NotBlank(message = "O email é obrigatório")
    @Column(name = "DS_EMAIL", nullable = false, unique = true, length = 200)
    @Schema(description = "Atributo do email do Tutor.")
    private String email;

    @Column(name = "NR_TELEFONE", length = 20)
    @Schema(description = "Atributo do telefone do Tutor.")
    private String telefone;

    @Column(name = "DS_ENDERECO", length = 300)
    @Schema(description = "Atributo do endereço do Tutor.")
    private String endereco;

    @Column(name = "DT_CADASTRO", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Atributo da data que o tutor foi registrado.")
    private LocalDate dataCadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private List<Pet> pets;

    @JsonIgnore
    @OneToMany(mappedBy = "tutor", fetch = FetchType.LAZY)
    private List<Notificacao> notificacoes;

    public void transferir(Tutor tutor) {
        this.nome = tutor.getNome();
        this.email = tutor.getEmail();
        this.telefone = tutor.getTelefone();
        this.endereco = tutor.getEndereco();
    }
}
