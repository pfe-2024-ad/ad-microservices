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
public class EmailSender {
    private String email;
    private String codeOtpEmail;
    private String subject;
    private Map<String, Object> variables;
    private String cheminTemplate;

}
