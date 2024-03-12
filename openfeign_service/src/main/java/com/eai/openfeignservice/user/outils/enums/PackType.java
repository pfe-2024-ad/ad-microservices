package com.eai.openfeignservice.user.outils.enums;

public enum PackType {

    CHEQUE_DH("Compte chèque en Dhs"),
    DHS_CONV("Compte en Dhs convertibles");
    private final String label;

    PackType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
