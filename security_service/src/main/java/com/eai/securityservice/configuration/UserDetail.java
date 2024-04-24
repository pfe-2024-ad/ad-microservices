package com.eai.securityservice.configuration;

import com.eai.openfeignservice.user.ClientResponseForSecurity;
import com.eai.openfeignservice.user.UserClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
public class UserDetail implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Override
    public UserDetails loadUserByUsername(String id) throws UsernameNotFoundException {
        Integer idClient = Integer.parseInt(id);
        ClientResponseForSecurity client = userClient.getClientForSecurity(idClient);

        if (client != null) {
            return new org.springframework.security.core.userdetails.User(
                    client.getEmail(),
                    "",
                    Collections.emptyList()
            );
        } else {
            throw new UsernameNotFoundException("User not found with idClient: " + idClient);
        }

    }
}
