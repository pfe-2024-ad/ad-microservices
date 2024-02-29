package com.eai.notification_service.outils.enums;

public enum EmailStatus {

    SUCCESSFUL("01"),
    ERROR("02");
    private final String label;

    EmailStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
