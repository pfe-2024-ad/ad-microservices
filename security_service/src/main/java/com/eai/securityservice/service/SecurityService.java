package com.eai.securityservice.service;

import com.eai.securityservice.configuration.JwtUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class SecurityService {


    public void validateTokenSecurity(String authorization) {

        System.out.println("vous avez valider le jwtToken yeeeeey");
    }


}
