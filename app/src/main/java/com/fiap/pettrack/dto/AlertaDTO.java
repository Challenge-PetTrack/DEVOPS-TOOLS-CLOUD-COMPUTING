package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import com.fiap.pettrack.model.enums.TipoAlertaEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO da entidade Alerta.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AlertaDTO {

    @NotNull(message = "O tipo de alerta é obrigatório")
    @Schema(description = "Tipo do alerta.", example = "FEBRE")
    private TipoAlertaEnum tipoAlerta;

    @Schema(description = "Descrição do alerta.", example = "Temperatura acima de 39.5°C")
    private String descricao;

    @Schema(description = "Valor de referência.", example = "39.8")
    private Double valorRef;

    @NotNull(message = "Informe se o alerta foi resolvido")
    @Schema(description = "Status de resolução.", example = "N")
    private SimNaoEnum resolvido;

    @Schema(description = "Objeto de pet.", example = "1")
    private Pet pet;
}