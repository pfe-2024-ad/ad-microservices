package com.eai.payment_service.outils.enums;

public enum PaymentStatus {
    SUCCESS("01"),
    AMOUNT_NOT_ACCEPTED("02"),

    PYMENT_ALREADY_MADE("03"),
    ERROR("04");

    private final String label;

    PaymentStatus(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
