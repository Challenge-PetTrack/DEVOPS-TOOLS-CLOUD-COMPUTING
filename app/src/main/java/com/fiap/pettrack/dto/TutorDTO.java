package com.fiap.pettrack.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Schema(description = "DTO da entidade Tutor.")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class TutorDTO {

    @NotBlank(message = "O nome é obrigatório")
    @Schema(description = "Nome do tutor.", example = "Ana Paula Silva")
    private String nome;

    @Email(message = "Informe um email válido")
    @NotBlank(message = "O email é obrigatório")
    @Schema(description = "Email do tutor.", example = "ana@email.com")
    private String email;

    @Schema(description = "Telefone do tutor.", example = "11975647387")
    private String telefone;

    @Schema(description = "Endereço do tutor.", example = "Rua Jose, 89")
    private String endereco;

}
