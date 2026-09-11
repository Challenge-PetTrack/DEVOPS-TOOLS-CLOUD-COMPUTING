package com.fiap.pettrack.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "Entidade que representa a tabela TB_BCS_HISTORICO no Oracle DB.")
@Entity
@Table(name = "TB_BCS_HISTORICO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class BCSHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_bcs")
    @SequenceGenerator(name = "seq_bcs", sequenceName = "SEQ_BCS_HIST", allocationSize = 1)
    @Column(name = "ID_BCS")
    @Schema(description = "Identificação do BCS.")
    private Long id;

    @Min(value = 1, message = "BCS mínimo é 1")
    @Max(value = 9, message = "BCS máximo é 9")
    @Column(name = "NR_BCS")
    @Schema(description = "Body Condition Score entre 1 e 9.")
    private Integer bcs;

    @Column(name = "DS_FOTO_URL", length = 500)
    @Schema(description = "URL da foto analisada.")
    private String fotoUrl;

    @Column(name = "DS_OBSERVACAO", length = 1000)
    @Schema(description = "Observações sobre o BCS.")
    private String observacao;

    @Column(name = "DT_ANALISE", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Data da análise do BCS.")
    private LocalDate dataAnalise;

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @Schema(description = "Pet relacionado ao BCS.")
    private Pet pet;

    public void transferir(BCSHistorico bcs) {
        this.bcs = bcs.getBcs();
        this.fotoUrl = bcs.getFotoUrl();
        this.observacao = bcs.getObservacao();
        this.pet = bcs.getPet();
    }
}