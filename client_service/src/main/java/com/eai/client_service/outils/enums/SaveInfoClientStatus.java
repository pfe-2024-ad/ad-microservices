package com.eai.client_service.outils.enums;

public enum SaveInfoClientStatus {

    SUCCESSFUL("01"),
    ERROR("02");
    private final String label;

    SaveInfoClientStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
