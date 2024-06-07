package com.eai.openfeignservice.user.outils.enums;

public enum ClientStep {
    EMAIL_STEP("EMAIL STEP"),
    PHONE_STEP("PHONE STEP"),
    OCR_STEP("OCR STEP"),
    VERIFICATION_STEP("VERIFICATION DONNEES STEP"),
    AGENCY_STEP("AGENCE STEP"),
    RECAP_STEP("RECAP STEP"),
    PAYMENT_STEP("PAYMENT STEP"),
    RDV_STEP("RDV STEP"),
    SPACE_STEP("MY SPACE STEP");

    private final String label;

    ClientStep(String label) {
        this.label = label;
    }
}
