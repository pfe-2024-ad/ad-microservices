package com.eai.securityservice.outiles.enums;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum StatusOTP {

        VALID("01"),
        INVALID("02"),
        EXPIRED_ATTEMPT("03"),
        TIMEOUT("04");
        private final String label;

}
