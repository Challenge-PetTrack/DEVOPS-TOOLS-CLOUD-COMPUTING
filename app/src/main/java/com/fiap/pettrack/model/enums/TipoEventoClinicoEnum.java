package com.fiap.pettrack.model.enums;

public enum TipoEventoClinicoEnum {
    CIRURGIA("Cirurgia"), CONSULTA("Consulta"), EXAME("Exame"), RETORNO("Retorno");

    private String disc;

    TipoEventoClinicoEnum(String disc){
        this.disc = disc;
    }

    public String getDisc(){
        return this.disc;
    }
}
