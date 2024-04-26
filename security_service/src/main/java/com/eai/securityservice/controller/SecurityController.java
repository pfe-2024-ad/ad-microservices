package com.eai.securityservice.controller;

import com.eai.securityservice.service.SecurityService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequiredArgsConstructor
public class SecurityController {


    private final SecurityService securityService;

    @PostMapping("/validateTokenService")
    public void validateToken(@RequestHeader("Authorization") String authorizationHeader) {
         securityService.validateTokenSecurity(authorizationHeader);
    }


}
