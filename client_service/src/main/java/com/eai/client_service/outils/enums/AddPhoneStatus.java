package com.eai.client_service.outils.enums;

public enum AddPhoneStatus {

    SUCCESSFUL("01"),
    ERROR("02");
    private final String label;

    AddPhoneStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
