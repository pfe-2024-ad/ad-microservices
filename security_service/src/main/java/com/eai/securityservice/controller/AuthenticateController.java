package com.eai.securityservice.controller;

import com.eai.securityservice.configuration.JwtUtil;
import com.eai.securityservice.configuration.UserDetail;
import com.eai.securityservice.dto.AuthenticateRequest;
import com.eai.securityservice.dto.AuthenticateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController {

    @Autowired
    private UserDetail userDetail;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/authenticate")
    public AuthenticateResponse createJwtToken(@RequestBody AuthenticateRequest authenticateRequest) throws Exception {
        return userDetail.createJwtToken(authenticateRequest);
    }

    @PostMapping("verifier-role")
    public String verifierRoleJwtToken(@RequestHeader("Authorization") String authorizationHeader) {

        String jwtToken = authorizationHeader.substring(7);
        return jwtUtil.getRoleFromToken(jwtToken);
    }
}
