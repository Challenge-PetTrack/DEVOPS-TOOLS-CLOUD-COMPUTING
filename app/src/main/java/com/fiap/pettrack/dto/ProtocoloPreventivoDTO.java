package com.fiap.pettrack.dto;

import com.fiap.pettrack.model.Pet;
import com.fiap.pettrack.model.enums.TipoProtocoloPreventivoEnum;
import com.fiap.pettrack.model.enums.StatusProtocoloPreventivoEnum;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Schema(description = "DTO da entidade Protocolo Preventivo.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ProtocoloPreventivoDTO {

    @NotNull(message = "O tipo de protocolo é obrigatório")
    @Schema(description = "Tipo do protocolo.", example = "VACINA")
    private TipoProtocoloPreventivoEnum tipo;

    @NotBlank(message = "O nome do protocolo é obrigatório")
    @Schema(description = "Nome do protocolo.", example = "V10")
    private String nome;

    @Schema(description = "Data de aplicação.", example = "2025-01-05")
    private LocalDate dataAplicacao;

    @Schema(description = "Data da próxima aplicação.", example = "2026-01-05")
    private LocalDate dataProxima;

    @NotNull(message = "O status é obrigatório")
    @Schema(description = "Status do protocolo.", example = "REALIZADO")
    private StatusProtocoloPreventivoEnum status;

    @Schema(description = "Objeto de pet.", example = "1")
    private Pet pet;
}