package com.eai.securityservice.configuration;

import com.eai.openfeignservice.config.ConfigClient;
import com.eai.openfeignservice.config.ParamDto;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

@Component
@RequiredArgsConstructor
public class JwtUtil {

    private final ConfigClient configClient;

    public String getEmailFromToken(String token) {

        return getClaimFromToken(token, Claims::getSubject);
    }

    public Integer getIdClientFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (Integer) claims.get("tokenId");
    }

    private Claims getAllClaimsFromToken(String token) {
        ParamDto paramDto = ParamDto.builder()
                .name("SECRET_KEY_PARAM")
                .build();
        final String SECRET_KEY = configClient.getParam(paramDto).getValue();

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


    public String generateToken(UserDetails userDetails, Integer idClient) {
        ParamDto paramDto = ParamDto.builder()
                .name("SECRET_KEY_PARAM")
                .build();
        final String SECRET_KEY = configClient.getParam(paramDto).getValue();

        ParamDto paramDto1 = ParamDto.builder()
                .name("TOKEN_VALIDITY_PARAM")
                .build();

        final Integer TOKEN_VALIDITY = Integer.parseInt(configClient.getParam(paramDto1).getValue());

        Map<String, Object> claims = new HashMap<>();
        claims.put("tokenId",idClient);


        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + TOKEN_VALIDITY * 1000))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }
}
