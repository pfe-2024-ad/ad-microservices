package com.eai.openfeignservice.user.outils.enums;

public enum PackOffres {

    CHEQUE_DH("Compte chèque en Dhs – 0 Dhs pendant 6 mois"),
    DHS_CONV("Compte chèque en Dhs convertible – 0 Dhs pendant 6 mois"),
    DEVISES("Compte en devises – 0 Dhs");
    private final String label;

    PackOffres(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
