package com.fiap.pettrack.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fiap.pettrack.model.enums.TipoEventoClinicoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.util.List;

@Schema(description = "Entidade que representa a tabela TB_EVENTO_CLINICO no Oracle DB.")
@Entity
@Table(name = "TB_EVENTO_CLINICO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class EventoClinico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_evento")
    @SequenceGenerator(name = "seq_evento", sequenceName = "SEQ_EVENTO_CLINICO", allocationSize = 1)
    @Column(name = "ID_EVENTO")
    @Schema(description = "Identificação do Evento Clínico.")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de evento é obrigatório")
    @Column(name = "TP_EVENTO", nullable = false, length = 50)
    @Schema(description = "Tipo do evento: CIRURGIA, CONSULTA, EXAME ou RETORNO.")
    private TipoEventoClinicoEnum tipo;

    @NotNull(message = "A data do evento é obrigatória")
    @Column(name = "DT_EVENTO", nullable = false)
    @Schema(description = "Data do evento clínico.")
    private LocalDate dataEvento;

    @Column(name = "DS_DIAGNOSTICO", length = 1000)
    @Schema(description = "Diagnóstico do evento.")
    private String diagnostico;

    @Column(name = "DS_OBSERVACAO", length = 2000)
    @Schema(description = "Observações do evento.")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @Schema(description = "Pet relacionado ao evento.")
    private Pet pet;

    @ManyToOne
    @JoinColumn(name = "ID_CLINICA", nullable = false)
    @Schema(description = "Clínica onde ocorreu o evento.")
    private Clinica clinica;

    @JsonIgnore
    @OneToMany(mappedBy = "evento", fetch = FetchType.LAZY)
    private List<Medicamento> medicamentos;

    public void transferir(EventoClinico evento) {
        this.tipo = evento.getTipo();
        this.dataEvento = evento.getDataEvento();
        this.diagnostico = evento.getDiagnostico();
        this.observacao = evento.getObservacao();
        this.pet = evento.getPet();
        this.clinica = evento.getClinica();
    }
}