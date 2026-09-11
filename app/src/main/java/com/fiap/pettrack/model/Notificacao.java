package com.fiap.pettrack.model;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoNotificacaoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "Entidade que representa a tabela TB_NOTIFICACAO no Oracle DB.")
@Entity
@Table(name = "TB_NOTIFICACAO")
@AllArgsConstructor
@NoArgsConstructor
@Data
public class Notificacao {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "seq_notificacao")
    @SequenceGenerator(name = "seq_notificacao", sequenceName = "SEQ_NOTIFICACAO", allocationSize = 1)
    @Column(name = "ID_NOTIFICACAO")
    @Schema(description = "Identificação da Notificação.")
    private Long id;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "O tipo de notificação é obrigatório")
    @Column(name = "TP_NOTIFICACAO", nullable = false, length = 50)
    @Schema(description = "Tipo da notificação: ALERTA, INFO, LEMBRETE ou URGENTE.")
    private TipoNotificacaoEnum tipo;

    @NotBlank(message = "O título é obrigatório")
    @Column(name = "DS_TITULO", nullable = false, length = 200)
    @Schema(description = "Título da notificação.")
    private String titulo;

    @Column(name = "DS_MENSAGEM", length = 2000)
    @Schema(description = "Mensagem da notificação.")
    private String mensagem;

    @Column(name = "DT_ENVIO", nullable = false, updatable = false, insertable = false)
    @Schema(description = "Data de envio da notificação.")
    private LocalDate dataEnvio;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Informe se a notificação foi lida")
    @Column(name = "ST_LIDA", nullable = false, length = 1)
    @Schema(description = "Indica se a notificação foi lida: S ou N.")
    private SimNaoEnum status;

    @ManyToOne
    @JoinColumn(name = "ID_TUTOR", nullable = false)
    @Schema(description = "Tutor destinatário da notificação.")
    private Tutor tutor;

    @ManyToOne
    @JoinColumn(name = "ID_PET", nullable = false)
    @Schema(description = "Pet relacionado à notificação.")
    private Pet pet;

    public void transferir(Notificacao notificacao) {
        this.tipo = notificacao.getTipo();
        this.titulo = notificacao.getTitulo();
        this.mensagem = notificacao.getMensagem();
        this.status = notificacao.getStatus();
        this.tutor = notificacao.getTutor();
        this.pet = notificacao.getPet();
    }
}