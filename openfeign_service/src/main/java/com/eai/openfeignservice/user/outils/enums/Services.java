package com.eai.openfeignservice.user.outils.enums;

public enum Services {
    CARNET("Compte sur carnet"),
    SALAMA("Salama (assistance)"),
    DABA_TRANSFER("DabaTransfer - Application de Transfert de BMCE EuroServices");

    private final String label;
    Services(String label){
        this.label = label;
    }
    public String getLabel(){
        return label;
    }
}
