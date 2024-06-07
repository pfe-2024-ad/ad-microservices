package com.eai.openfeignservice.administrateur.outils.enums;

public enum Role {
    ADMIN("ADMIN"),
    AGENT("AGENT"),
    CLIENT("CLIENT");

    private final String label;
    Role(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
}
