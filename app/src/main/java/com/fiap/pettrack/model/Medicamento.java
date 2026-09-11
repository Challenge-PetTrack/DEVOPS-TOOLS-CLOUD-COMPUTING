package com.fiap.pettrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Entidade que representa a tabela TB_MEDICAMENTO no Oracle DB.")
@Entity
@Table(name = "TB_MEDICAMENTO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_medicamento")
    @SequenceGenerator(name = "seq_medicamento", sequenceName = "SEQ_MEDICAMENTO", allocationSize = 1)
    @Column(name = "ID_MEDICAMENTO")
    @Schema(description = "Identificação do Medicamento.")
    private Long id;

    @NotBlank(message = "O nome do medicamento é obrigatório")
    @Column(name = "NM_MEDICAMENTO", nullable = false, length = 200)
    @Schema(description = "Nome do medicamento.")
    private String nome;

    @NotBlank(message = "A dosagem é obrigatória")
    @Column(name = "DS_DOSAGEM", nullable = false, length = 100)
    @Schema(description = "Dosagem do medicamento.")
    private String dosagem;

    @NotBlank(message = "A frequência é obrigatória")
    @Column(name = "DS_FREQUENCIA", nullable = false, length = 100)
    @Schema(description = "Frequência de administração.")
    private String frequencia;

    @NotNull(message = "A data de início é obrigatória")
    @Column(name = "DT_INICIO", nullable = false)
    @Schema(description = "Data de início do medicamento.")
    private LocalDate dataInicio;

    @Column(name = "DT_FIM")
    @Schema(description = "Data de fim do medicamento.")
    private LocalDate dataFim;

    @ManyToOne
    @JoinColumn(name = "ID_EVENTO", nullable = false)
    @Schema(description = "Evento clínico que originou o medicamento.")
    private EventoClinico evento;

    @JsonIgnore
    @OneToMany(mappedBy = "medicamento", fetch = FetchType.LAZY)
    private List<AdesaoMedicamento> adesoes;

    public void transferir(Medicamento medicamento) {
        this.nome = medicamento.getNome();
        this.dosagem = medicamento.getDosagem();
        this.frequencia = medicamento.getFrequencia();
        this.dataInicio = medicamento.getDataInicio();
        this.dataFim = medicamento.getDataFim();
        this.evento = medicamento.getEvento();
    }
}