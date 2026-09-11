package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Pet;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO da entidade BCS Histórico.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BCSHistoricoDTO {

    @Min(value = 1, message = "BCS mínimo é 1")
    @Max(value = 9, message = "BCS máximo é 9")
    @Schema(description = "Body Condition Score entre 1 e 9.", example = "5")
    private Integer bcs;

    @Schema(description = "URL da foto analisada.", example = "http://pettrack.com/fotos/thor1.jpg")
    private String fotoUrl;

    @Schema(description = "Observações.", example = "BCS ideal")
    private String observacao;

    @Schema(description = "Objeto de pet.", example = "1")
    private Pet pet;
}