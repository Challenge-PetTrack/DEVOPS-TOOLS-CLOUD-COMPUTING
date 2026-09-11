package com.fiap.pettrack.model.enums;

public enum SimNaoEnum {
    S("Sim"), N("Não");
    private String desc;

    SimNaoEnum(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }
}
