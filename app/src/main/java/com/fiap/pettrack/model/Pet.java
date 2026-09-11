package com.fiap.pettrack.model;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.pettrack.model.enums.SexoPetEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Entidade que representa a tabela TB_PET no Oracle DB.")
@Entity
@Table(name = "TB_PET")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Pet {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_pet")
    @SequenceGenerator(name = "seq_pet", sequenceName = "SEQ_PET", allocationSize = 1)
    @Column(name = "ID_PET")
    @Schema(description = "Identificação do Pet.")
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Column(name = "NM_PET", nullable = false, length = 100)
    @Schema(description = "Nome do Pet.")
    private String nome;

    @NotBlank(message = "A espécie é obrigatória")
    @Column(name = "DS_ESPECIE", nullable = false, length = 50)
    @Schema(description = "Espécie do Pet.")
    private String especie;

    @Column(name = "DS_RACA", length = 100)
    @Schema(description = "Raça do Pet.")
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(name = "DS_SEXO", length = 1)
    @Schema(description = "Sexo do Pet: M ou F.")
    private SexoPetEnum sexo;

    @Column(name = "NR_IDADE_ANOS")
    @Schema(description = "Idade do Pet em anos.")
    private Double idade;

    @Column(name = "NR_PESO_KG")
    @Schema(description = "Peso do Pet em kg.")
    private Double peso;

    @Column(name = "DT_CADASTRO", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Data de cadastro do Pet.")
    private LocalDate dataCadastro;

    @ManyToOne
    @JoinColumn(name = "ID_TUTOR", nullable = false)
    @Schema(description = "Tutor responsável pelo Pet.")
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "ID_CLINICA", nullable = false)
    @Schema(description = "Clínica vinculada ao Pet.")
    private Clinica clinica;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<EventoClinico> eventos;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<ProtocoloPreventivo> protocolos;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<Notificacao> notificacoes;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<ScoreHistorico> scores;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<BCSHistorico> bcsHistoricos;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<CollarLeitura> collarLeituras;

    @JsonIgnore
    @OneToMany(mappedBy = "pet", fetch = FetchType.LAZY)
    private List<Alerta> alertas;

    public void transferir(Pet pet) {
        this.nome = pet.getNome();
        this.especie = pet.getEspecie();
        this.raca = pet.getRaca();
        this.sexo = pet.getSexo();
        this.idade = pet.getIdade();
        this.peso = pet.getPeso();
        this.tutor = pet.getTutor();
        this.clinica = pet.getClinica();
    }
}