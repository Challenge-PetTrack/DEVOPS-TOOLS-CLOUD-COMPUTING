package com.fiap.pettrack.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "Entidade que representa a tabela TB_SCORE_HISTORICO no Oracle DB.")
@Entity
@Table(name = "TB_SCORE_HISTORICO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class ScoreHistorico {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_score")
    @SequenceGenerator(name = "seq_score", sequenceName = "SEQ_SCORE_HIST", allocationSize = 1)
    @Column(name = "ID_SCORE")
    @Schema(description = "Identificação do Score.")
    private Long id;

    @NotNull(message = "O score é obrigatório")
    @DecimalMin(value = "0.0", message = "Score mínimo é 0")
    @DecimalMax(value = "100.0", message = "Score máximo é 100")
    @Column(name = "NR_SCORE", nullable = false)
    @Schema(description = "Valor do health score entre 0 e 100.")
    private Double score;

    @Column(name = "DT_REGISTRO", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Data do registro do score.")
    private LocalDate dataRegistro;

    @Column(name = "DS_OBSERVACAO", length = 500)
    @Schema(description = "Observações sobre o score.")
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @Schema(description = "Pet relacionado ao score.")
    private Pet pet;

    public void transferir(ScoreHistorico score) {
        this.score = score.getScore();
        this.observacao = score.getObservacao();
        this.pet = score.getPet();
    }
}