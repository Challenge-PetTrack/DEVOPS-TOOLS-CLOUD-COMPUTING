package com.fiap.pettrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO da entidade Clínica.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ClinicaDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome da clínica.", example = "VetLife Clínica Veterinária")
    private String nome;

    @NotBlank(message = "O CNPJ é obrigatório")
    @Schema(description = "CNPJ da clínica.", example = "75.874.894/0001-23")
    private String cnpj;

    @Email(message = "Informe um email válido")
    @Schema(description = "Email da clínica.", example = "vetlife@email.com")
    private String email;

    @Schema(description = "Telefone da clínica.", example = "11987654321")
    private String telefone;

    @Schema(description = "Endereço da clínica.", example = "Rua Gamelinha, 99")
    private String endereco;
}