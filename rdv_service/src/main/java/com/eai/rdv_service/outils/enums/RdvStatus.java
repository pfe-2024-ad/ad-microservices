package com.eai.rdv_service.outils.enums;

public enum RdvStatus {
    A_FAIRE("Le client choisit un rendez-vous"),
    A_CONVENIR("Aucun rendez-vous ne convient le client");

    private final String label;

    RdvStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}