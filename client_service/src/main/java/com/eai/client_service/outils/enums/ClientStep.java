package com.eai.client_service.outils.enums;

public enum ClientStep {
    EMAIL_STEP("EMAIL STEP"),
    PHONE_STEP("PHONE STEP"),
    OCR_STEP("OCR STEP"),
    AGENCY_STEP("AGENCE STEP"),
    PAYMENT_STEP("PAYMENT STEP"),
    RDV_STEP("RDV STEP");

    private final String label;

    ClientStep(String label) {
        this.label = label;
    }
}
