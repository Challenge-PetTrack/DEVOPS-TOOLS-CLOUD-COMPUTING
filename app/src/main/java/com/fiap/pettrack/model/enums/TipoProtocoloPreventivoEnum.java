package com.fiap.pettrack.model.enums;

public enum TipoProtocoloPreventivoEnum {
    ANTIPULGA("Antipulga"),CHECKUP("Checkup"),VACINA("Vacina"), VERMIFUGO("Vermifugo");

    private String disc;
    TipoProtocoloPreventivoEnum(String disc){
        this.disc = disc;
    }

    public String getDisc(){
        return this.disc;
    }
}
