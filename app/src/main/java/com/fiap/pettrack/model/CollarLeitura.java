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

@Schema(description = "Entidade que representa a tabela TB_COLLAR_LEITURA no Oracle DB.")
@Entity
@Table(name = "TB_COLLAR_LEITURA")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class CollarLeitura {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_collar")
    @SequenceGenerator(name = "seq_collar", sequenceName = "SEQ_COLLAR", allocationSize = 1)
    @Column(name = "ID_LEITURA")
    @Schema(description = "Identificação da leitura do collar.")
    private Long id;

    @NotNull(message = "A temperatura é obrigatória")
    @DecimalMin(value = "30.0", message = "Temperatura mínima é 30°C")
    @DecimalMax(value = "45.0", message = "Temperatura máxima é 45°C")
    @Column(name = "NR_TEMPERATURA", nullable = false)
    @Schema(description = "Temperatura corporal entre 30 e 45°C.")
    private Double temperatura;

    @Column(name = "NR_ATIVIDADE")
    @Schema(description = "Nível de atividade do pet.")
    private Double atividade;

    @Column(name = "DT_LEITURA", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Data da leitura.")
    private LocalDate dataLeitura;

    @Column(name = "DS_TOPICO_MQTT", length = 200)
    @Schema(description = "Tópico MQTT de origem da leitura.")
    private String topicoMqtt;

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @Schema(description = "Pet relacionado à leitura.")
    private Pet pet;

    public void transferir(CollarLeitura collar) {
        this.temperatura = collar.getTemperatura();
        this.atividade = collar.getAtividade();
        this.topicoMqtt = collar.getTopicoMqtt();
        this.pet = collar.getPet();
    }
}