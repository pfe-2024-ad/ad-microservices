package com.eai.notification_service.outils.enums;

public enum SmsStatus {

    SUCCESSFUL("01"),
    ERROR("02");

    private final String label;

    SmsStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }

}
