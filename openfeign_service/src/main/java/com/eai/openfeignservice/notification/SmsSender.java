package com.eai.openfeignservice.notification;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class SmsSender {
    private String indicatifTel;
    private String numTel;
    private String codeOtpSms;

}
