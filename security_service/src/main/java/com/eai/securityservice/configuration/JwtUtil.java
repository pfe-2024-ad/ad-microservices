package com.eai.securityservice.configuration;

import com.eai.openfeignservice.config.ConfigClient;
import com.eai.openfeignservice.config.ParamDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.core.GrantedAuthority;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final ConfigClient securityConfigClient;

    public String getEmailFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        List<String> roles = (List<String>) claims.get("roles");
        return roles.get(0);
    }

    private Claims getAllClaimsFromToken(String token) {
        //get SECRET_KEY_TOKEN from configuration service
        ParamDto paramDto = ParamDto.builder()
                .name("SECRET_KEY_TOKEN")
                .build();
        final String SECRET_KEY =securityConfigClient.getParam(paramDto).getValue();

        return Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token).getBody();
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimsResolver.apply(claims);
    }

    public Boolean validateToken(String token, UserDetails userDetails) {
        final String username = getEmailFromToken(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public Date getExpirationDateFromToken(String token) {

        return getClaimFromToken(token, Claims::getExpiration);
    }

    private Boolean isTokenExpired(String token) {
        final Date expiration = getExpirationDateFromToken(token);
        return expiration.before(new Date());
    }


    public String generateToken(UserDetails userDetails) {

        ParamDto paramDto1 = ParamDto.builder()
                .name("TOKEN_VALIDITY_PARAM")
                .build();

        final Integer TOKEN_VALIDITY = Integer.parseInt(securityConfigClient.getParam(paramDto1).getValue());

        Map<String, Object> claims = new HashMap<>();

        // Add roles to claims
        List<String> roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        claims.put("roles", roles);

        //get SECRET_KEY_TOKEN from configuration service
        ParamDto paramDto = ParamDto.builder()
                .name("SECRET_KEY_TOKEN")
                .build();
        final String SECRET_KEY =securityConfigClient.getParam(paramDto).getValue();


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
