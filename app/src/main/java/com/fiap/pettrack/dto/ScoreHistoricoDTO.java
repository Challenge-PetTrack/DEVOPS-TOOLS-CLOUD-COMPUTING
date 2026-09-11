package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO da entidade Score Histórico.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ScoreHistoricoDTO {

    @NotNull(message = "O score é obrigatório")
    @DecimalMin(value = "0.0", message = "Score mínimo é 0")
    @DecimalMax(value = "100.0", message = "Score máximo é 100")
    @Schema(description = "Valor do health score.", example = "75.0")
    private Double score;

    @Schema(description = "Observações.", example = "Score inicial")
    private String observacao;

    @Schema(description = "Objeto de pet.", example = "1")
    private Pet pet;
}