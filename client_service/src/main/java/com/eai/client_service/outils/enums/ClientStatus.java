package com.eai.client_service.outils.enums;

public enum ClientStatus {
    PRE_PROSPECT("Pré Prospect"),
    PROSPECT("Prospect"),
    PROSPECT_FINALISE("Prospect finalisé"),
    EQUIPPED("Equipé"),
    CLIENT_BANQUE_AGENCE("Client Banque Agence"),
    CLIENT_BANQUE_EKYC("Client Banque E-KYC"),
    REQUEST_OFF("Demande OFF"),
    AGD("AGD");

    private final String label;

    ClientStatus(String label) {
        this.label = label;
    }

}
