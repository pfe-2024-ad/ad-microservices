package com.eai.enums;


public enum Payment_Status {

    SUCCESS("SUCCESS"),

    FAILED("FAILED"),

    CANCEL("CANCEL");
   private final String label;

    Payment_Status(String label) {
        this.label = label;

    }
        public String getLabel () {
            return label;
        }

}