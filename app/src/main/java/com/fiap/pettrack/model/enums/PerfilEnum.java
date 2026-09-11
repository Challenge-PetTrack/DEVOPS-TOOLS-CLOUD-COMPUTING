package com.fiap.pettrack.model.enums;

public enum PerfilEnum {
    ROLE_ADMIN("Administrador"),
    ROLE_VET("Veterinário"),
    ROLE_TUTOR("Tutor");

    private final String descricao;

    PerfilEnum(String descricao) {
        this.descricao = descricao;
    }

    public String getDescricao() {
        return descricao;
    }
}
