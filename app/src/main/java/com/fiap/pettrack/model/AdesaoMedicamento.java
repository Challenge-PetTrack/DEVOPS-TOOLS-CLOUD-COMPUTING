package com.fiap.pettrack.model;

import com.fiap.pettrack.model.enums.SimNaoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "Entidade que representa a tabela TB_ADESAO_MEDICAMENTO no Oracle DB.")
@Entity
@Table(name = "TB_ADESAO_MEDICAMENTO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class AdesaoMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_adesao")
    @SequenceGenerator(name = "seq_adesao", sequenceName = "SEQ_ADESAO", allocationSize = 1)
    @Column(name = "ID_ADESAO")
    @Schema(description = "Identificação da Adesão.")
    private Long id;

    @NotNull(message = "A data da dose é obrigatória")
    @Column(name = "DT_DOSE", nullable = false)
    @Schema(description = "Data em que a dose foi ou deveria ser administrada.")
    private LocalDate dataDose;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe se o medicamento foi tomado")
    @Column(name = "ST_TOMOU", nullable = false, length = 1)
    @Schema(description = "Indica se o medicamento foi tomado: S ou N.")
    private SimNaoEnum status;

    @Column(name = "DS_OBSERVACAO", length = 500)
    @Schema(description = "Observações sobre a adesão.")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "ID_MEDICAMENTO", nullable = false)
    @Schema(description = "Medicamento relacionado à adesão.")
    private Medicamento medicamento;

    public void transferir(AdesaoMedicamento adesao) {
        this.dataDose = adesao.getDataDose();
        this.status = adesao.getStatus();
        this.observacao = adesao.getObservacao();
        this.medicamento = adesao.getMedicamento();
    }
}