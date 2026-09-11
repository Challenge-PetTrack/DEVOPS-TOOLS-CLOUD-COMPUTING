package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO da entidade Collar Leitura.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CollarLeituraDTO {

    @NotNull(message = "A temperatura é obrigatória")
    @DecimalMin(value = "30.0", message = "Temperatura mínima é 30°C")
    @DecimalMax(value = "45.0", message = "Temperatura máxima é 45°C")
    @Schema(description = "Temperatura corporal.", example = "38.5")
    private Double temperatura;

    @Schema(description = "Nível de atividade.", example = "120.0")
    private Double atividade;

    @Schema(description = "Tópico MQTT.", example = "pettrack/collar/pet1")
    private String topicoMqtt;

    @Schema(description = "Objeto de pet.", example = "1")
    private Pet pet;
}