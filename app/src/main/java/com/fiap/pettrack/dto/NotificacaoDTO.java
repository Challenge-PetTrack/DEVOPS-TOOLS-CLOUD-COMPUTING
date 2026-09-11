package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.Tutor;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoNotificacaoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO da entidade Notificação.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class NotificacaoDTO {

    @NotNull(message = "O tipo de notificação é obrigatório")
    @Schema(description = "Tipo da notificação.", example = "LEMBRETE")
    private TipoNotificacaoEnum tipo;

    @NotBlank(message = "O título é obrigatório")
    @Schema(description = "Título da notificação.", example = "Vacina do Thor")
    private String titulo;

    @Schema(description = "Mensagem da notificação.", example = "Vacina V10 vence em 30 dias")
    private String mensagem;

    @NotNull(message = "Informe se a notificação foi lida")
    @Schema(description = "Status de leitura.", example = "N")
    private SimNaoEnum status;

    @Schema(description = "Objeto de Tutor.", example = "1")
    private Tutor tutor;

    @Schema(description = "Objeto de pet.", example = "1")
    private Pet pet;
}