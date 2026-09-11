package com.fiap.pettrack.model.enums;

public enum TipoAlertaEnum {
    ADESAO("Adesao"), BCS_CRITICO("Bcs_critico"), FEBRE("Febre"), PESO("Peso"), SEDENTARISMO("Sedentarismo");
    private String desc;

    TipoAlertaEnum(String desc){
        this.desc = desc;
    }
    public String getDesc(){
        return this.desc;
    }
}
