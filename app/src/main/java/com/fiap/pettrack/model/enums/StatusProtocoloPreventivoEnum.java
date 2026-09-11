package com.fiap.pettrack.model.enums;

public enum StatusProtocoloPreventivoEnum {
    ATRASADO("Atrasado"), PENDENTE("Pendente"), REALIZADO("Realizado");

    private String disc;

    StatusProtocoloPreventivoEnum(String disc){
        this.disc = disc;
    }

    public String getDisc(){
        return this.disc;
    }
}
