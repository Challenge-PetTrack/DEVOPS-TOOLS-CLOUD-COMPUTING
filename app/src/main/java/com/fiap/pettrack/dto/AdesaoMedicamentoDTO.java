package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Medicamento;
import com.fiap.pettrack.model.enums.SimNaoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "DTO da entidade Adesão ao Medicamento.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AdesaoMedicamentoDTO {

    @NotNull(message = "A data da dose é obrigatória")
    @Schema(description = "Data da dose.", example = "2025-01-11")
    private LocalDate dataDose;

    @NotNull(message = "Informe se o medicamento foi tomado")
    @Schema(description = "Indica se tomou: S ou N.", example = "S")
    private SimNaoEnum status;

    @Schema(description = "Observações.", example = "Tutor esqueceu")
    private String observacao;

    @Schema(description = "Objeto de medicamento.", example = "1")
    private Medicamento medicamento;
}