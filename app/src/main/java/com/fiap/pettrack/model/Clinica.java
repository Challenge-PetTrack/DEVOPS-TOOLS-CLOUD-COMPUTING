package com.fiap.pettrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Entidade que representa a tabela TB_CLINICA no Oracle DB.")
@Entity
@Table(name = "TB_CLINICA")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Clinica {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_clinica")
    @SequenceGenerator(name = "seq_clinica", sequenceName = "SEQ_CLINICA", allocationSize = 1)
    @Column(name = "ID_CLINICA")
    @Schema(description = "Atributo de identificação da Clínica.")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "NM_CLINICA", nullable = false, length = 200)
    @Schema(description = "Nome da clínica.")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Column(name = "NR_CNPJ", nullable = false, unique = true, length = 18)
    @Schema(description = "CNPJ da clínica.")
    private String cnpj;

    @Email(message = "Informe um email válido")
    @Column(name = "DS_EMAIL", length = 200)
    @Schema(description = "Email da clínica.")
    private String email;

    @Column(name = "NR_TELEFONE", length = 20)
    @Schema(description = "Telefone da clínica.")
    private String telefone;

    @Column(name = "DS_ENDERECO", length = 300)
    @Schema(description = "Endereço da clínica.")
    private String endereco;

    @Column(name = "DT_CADASTRO", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Data de cadastro da clínica.")
    private LocalDate dataCadastro;

    @JsonIgnore
    @OneToMany(mappedBy = "clinica", fetch = FetchType.LAZY)
    private List<Pet> pets;

    @JsonIgnore
    @OneToMany(mappedBy = "clinica", fetch = FetchType.LAZY)
    private List<EventoClinico> eventos;

    public void transferir(Clinica clinica) {
        this.nome = clinica.getNome();
        this.cnpj = clinica.getCnpj();
        this.email = clinica.getEmail();
        this.telefone = clinica.getTelefone();
        this.endereco = clinica.getEndereco();
    }
}