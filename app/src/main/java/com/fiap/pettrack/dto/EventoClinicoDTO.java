package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Clinica;
import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.enums.TipoEventoClinicoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "DTO da entidade Evento Clínico.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EventoClinicoDTO {

    @NotNull(message = "O tipo de evento é obrigatório")
    @Schema(description = "Tipo do evento.", example = "CONSULTA")
    private TipoEventoClinicoEnum tipo;

    @NotNull(message = "A data do evento é obrigatória")
    @Schema(description = "Data do evento.", example = "2025-01-10")
    private LocalDate dataEvento;

    @Schema(description = "Diagnóstico do evento.", example = "Dermatite alérgica")
    private String diagnostico;

    @Schema(description = "Observações do evento.", example = "Coceira intensa")
    private String observacao;

    @Schema(description = "Objeto de pet.", example = "1")
    private Pet pet;

    @Schema(description = "Objeto de clínica.", example = "1")
    private Clinica clinica;
}