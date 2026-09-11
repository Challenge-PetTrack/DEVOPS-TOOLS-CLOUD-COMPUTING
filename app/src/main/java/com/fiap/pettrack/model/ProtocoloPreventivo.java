package com.fiap.pettrack.model;

import com.fiap.pettrack.model.enums.StatusProtocoloPreventivoEnum;
import com.fiap.pettrack.model.enums.TipoProtocoloPreventivoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Data
@Schema(description = "Entidade que representa a tabela TB_PROTOCOLO_PREVENTIVO no Oracle DB.")
@Entity
@Table(name = "TB_PROTOCOLO_PREVENTIVO")
@AllArgsConstructor
@NoArgsConstructor
public class ProtocoloPreventivo {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_protocolo")
    @SequenceGenerator(name = "seq_protocolo", sequenceName = "SEQ_PROTOCOLO", allocationSize = 1)
    @Column(name = "ID_PROTOCOLO")
    @Schema(description = "Identificação do Protocolo Preventivo.")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de protocolo é obrigatório")
    @Column(name = "TP_PROTOCOLO", nullable = false, length = 50)
    @Schema(description = "Tipo do protocolo: ANTIPULGA, CHECKUP, VACINA ou VERMIFUGO.")
    private TipoProtocoloPreventivoEnum tipo;

    @NotBlank(message = "O nome do protocolo é obrigatório")
    @Column(name = "NM_PROTOCOLO", nullable = false, length = 200)
    @Schema(description = "Nome do protocolo.")
    private String nome;

    @Column(name = "DT_APLICACAO")
    @Schema(description = "Data de aplicação do protocolo.")
    private LocalDate dataAplicacao;

    @Column(name = "DT_PROXIMA")
    @Schema(description = "Data da próxima aplicação.")
    private LocalDate dataProxima;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O status é obrigatório")
    @Column(name = "ST_STATUS", nullable = false, length = 20)
    @Schema(description = "Status do protocolo: ATRASADO, PENDENTE ou REALIZADO.")
    private StatusProtocoloPreventivoEnum status;

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @Schema(description = "Pet relacionado ao protocolo.")
    private Pet pet;

    public void transferir(ProtocoloPreventivo protocolo) {
        this.tipo = protocolo.getTipo();
        this.nome = protocolo.getNome();
        this.dataAplicacao = protocolo.getDataAplicacao();
        this.dataProxima = protocolo.getDataProxima();
        this.status = protocolo.getStatus();
        this.pet = protocolo.getPet();
    }
}