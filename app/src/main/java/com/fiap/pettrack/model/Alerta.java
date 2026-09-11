package com.fiap.pettrack.model;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoAlertaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "Entidade que representa a tabela TB_ALERTA no Oracle DB.")
@Entity
@Table(name = "TB_ALERTA")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Alerta {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_alerta")
    @SequenceGenerator(name = "seq_alerta", sequenceName = "SEQ_ALERTA", allocationSize = 1)
    @Column(name = "ID_ALERTA")
    @Schema(description = "Identificação do Alerta.")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de alerta é obrigatório")
    @Column(name = "TP_ALERTA", nullable = false, length = 50)
    @Schema(description = "Tipo do alerta: ADESAO, BCS_CRITICO, FEBRE, PESO ou SEDENTARISMO.")
    private TipoAlertaEnum tipoAlerta;

    @Column(name = "DS_DESCRICAO", length = 1000)
    @Schema(description = "Descrição do alerta.")
    private String descricao;

    @Column(name = "NR_VALOR_REF")
    @Schema(description = "Valor de referência que gerou o alerta.")
    private Double valorRef;

    @Column(name = "DT_ALERTA", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Data do alerta.")
    private LocalDate dataAlerta;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe se o alerta foi resolvido")
    @Column(name = "ST_RESOLVIDO", nullable = false, length = 1)
    @Schema(description = "Indica se o alerta foi resolvido: S ou N.")
    private SimNaoEnum resolvido;

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @Schema(description = "Pet relacionado ao alerta.")
    private Pet pet;

    public void transferir(Alerta alerta) {
        this.tipoAlerta = alerta.getTipoAlerta();
        this.descricao = alerta.getDescricao();
        this.valorRef = alerta.getValorRef();
        this.resolvido = alerta.getResolvido();
        this.pet = alerta.getPet();
    }
}