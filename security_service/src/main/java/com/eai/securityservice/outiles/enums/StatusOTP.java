package com.eai.securityservice.outiles.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusOTP {

        VALIDE("01"),
        INVALID("02"),
        EXPIRED("03"),
        TIMEOUT("04");
        private final String label;

}