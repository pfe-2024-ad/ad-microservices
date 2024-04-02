package com.eai.client_service.outils.enums;

public enum OcrStatus {

    SUCCESSFUL("01"),
    ERROR("02");
    private final String label;

    OcrStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
