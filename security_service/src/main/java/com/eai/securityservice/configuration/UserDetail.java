package com.eai.securityservice.configuration;

import com.eai.openfeignservice.administrateur.AdminClient;
import com.eai.openfeignservice.administrateur.AdminResponseForSecurity;
import com.eai.openfeignservice.administrateur.outils.enums.Role;
import com.eai.openfeignservice.user.ClientResponseForSecurity;
import com.eai.openfeignservice.user.UserClient;
import com.eai.securityservice.dto.AuthenticateRequest;
import com.eai.securityservice.dto.AuthenticateResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Set;

@Service
@Slf4j
public class UserDetail implements UserDetailsService {

    @Autowired
    private UserClient userClient;

    @Autowired
    private AdminClient adminClient;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private AuthenticationManager authenticationManager;

    public AuthenticateResponse createJwtToken(AuthenticateRequest authenticateRequest) throws Exception {

        String userName = authenticateRequest.getEmail();
        String userPassword = authenticateRequest.getMotDePasse();
        authenticate(userName, userPassword);

        UserDetails userDetails = loadUserByUsername(userName);

        String newGeneratedToken = jwtUtil.generateToken(userDetails);

        AdminResponseForSecurity  admin = adminClient.getAdminForSecurity(authenticateRequest.getEmail());

        return new AuthenticateResponse(admin.getEmail(),admin.getRole(), newGeneratedToken);
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        ClientResponseForSecurity client = userClient.getClientForSecurity(email);

        AdminResponseForSecurity  admin = adminClient.getAdminForSecurity(email);

        if (client.getEmail() != null) {
            return new org.springframework.security.core.userdetails.User(
                    client.getEmail(),
                    "",
                    getAuthorityClient(client)
            );
        } else if (admin.getEmail() != null) {

            return new org.springframework.security.core.userdetails.User(
                    admin.getEmail(),
                    admin.getMotDePasse(),
                    getAuthorityAdmin(admin)
            );
        } else {
            throw new UsernameNotFoundException("User not found with idClient: " + email);
        }
    }

    private Set getAuthorityClient(ClientResponseForSecurity client) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Role role = client.getRole();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

    private Set getAuthorityAdmin(AdminResponseForSecurity admin) {
        Set<SimpleGrantedAuthority> authorities = new HashSet<>();
        Role role = admin.getRole();
        authorities.add(new SimpleGrantedAuthority("ROLE_" + role));
        return authorities;
    }

    private void authenticate(String userName, String userPassword) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userName, userPassword));
            log.info("authenticate passe bien");
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}
