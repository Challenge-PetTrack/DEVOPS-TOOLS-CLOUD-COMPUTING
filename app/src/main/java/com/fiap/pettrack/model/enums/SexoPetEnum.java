package com.fiap.pettrack.model.enums;

public enum SexoPetEnum {
    F("Feminino"), M("Masculino");

    private String desc;

    SexoPetEnum(String desc){
        this.desc = desc;
    }

    public String getDesc(){
        return this.desc;
    }
}
