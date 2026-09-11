package com.fiap.pettrack.model.enums;

public enum TipoNotificacaoEnum {
    ALERTA("Alerta"), INFO("Informação"), LEMBRETE("Lembrete"), URGENTE("Urgente");
    private String desc;
    TipoNotificacaoEnum(String disc){
        this.desc = disc;
    }
    public String getDesc(){
        return this.desc;
    }
}
