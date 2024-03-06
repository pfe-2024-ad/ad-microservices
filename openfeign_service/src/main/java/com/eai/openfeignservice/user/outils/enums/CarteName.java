package com.eai.openfeignservice.user.outils.enums;

public enum CarteName {
    BLEU_NATIONAL("Carte Blue Nationale"),
    BLEU_INTERNATIONAL("Carte Blue Internationale"),
    FIRST("Carte First"),
    FAMILY("Carte Family");

    private final String label;
    CarteName(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
}
