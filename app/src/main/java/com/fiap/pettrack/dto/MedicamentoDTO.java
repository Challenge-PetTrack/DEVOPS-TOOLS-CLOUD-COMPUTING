package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.EventoClinico;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "DTO da entidade Medicamento.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class MedicamentoDTO {

    @NotBlank(message = "O nome do medicamento é obrigatório")
    @Schema(description = "Nome do medicamento.", example = "Apoquel 16mg")
    private String nome;

    @NotBlank(message = "A dosagem é obrigatória")
    @Schema(description = "Dosagem do medicamento.", example = "1 comprimido")
    private String dosagem;

    @NotBlank(message = "A frequência é obrigatória")
    @Schema(description = "Frequência de administração.", example = "1x ao dia")
    private String frequencia;

    @NotNull(message = "A data de início é obrigatória")
    @Schema(description = "Data de início.", example = "2025-01-10")
    private LocalDate dataInicio;

    @Schema(description = "Data de fim.", example = "2025-02-10")
    private LocalDate dataFim;

    @Schema(description = "Obejto de evento clínico.", example = "1")
    private EventoClinico eventoClinico;
}