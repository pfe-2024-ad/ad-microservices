package com.eai.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class AuthenticationFilter  extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RestTemplate restTemplate;


    public AuthenticationFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        return ((exchange, chain) -> {
                //header contains token or not
                if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                    log.info("header authorization n'existe pas");
                    return Mono.error(new RuntimeException("missing authorization header"));
                }

                String requestTokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");

                log.info("!!!!!!!!!!");
                log.info(requestTokenHeader);

                if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                    String jwtToken = requestTokenHeader.substring(7);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(jwtToken);

                    log.info("here1");
                    log.info(headers.toString());
                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    restTemplate.postForEntity(
                            "http://localhost:7777/agd/security-service/validateToken",
                            entity,
                            Void.class
                    );
                    log.info("good");
                    return chain.filter(exchange);
                }


                log.info("requestTokenHeader est null");
            return chain.filter(exchange);
        });
    }


    public static class Config {

    }

/*

    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        if (routeValidator.isSecured.test(exchange.getRequest())) {
            ServerHttpRequest request = exchange.getRequest();
            String requestTokenHeader = request.getHeaders().getFirst("Authorization");


            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {

                String jwtToken = requestTokenHeader.substring(7);

                HttpHeaders headers = new HttpHeaders();
                headers.setBearerAuth(jwtToken);

                log.info("here1");
                log.info(jwtToken);
                log.info(headers.toString());
                //voici le resultat de ca: [Authorization:"Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJib3Vzc0BnbWFpb..]

                /*
                HttpEntity<String> entity = new HttpEntity<>(headers);
                ResponseEntity<String> responseEntity = restTemplate.postForEntity(
                        "http://localhost:9090/agd/security-service/get-email-from-token",
                        entity,
                        String.class
                );

                // Traitez la réponse ici
                String emailFromToken = responseEntity.getBody();


                SecurityRequestForAuthFilter securityRequest = new SecurityRequestForAuthFilter();
                securityRequest.setJwtToken(jwtToken);



                log.info("here2");
                log.info(emailFromToken);
                securityRequest.setEmailToken(emailFromToken);



 //here end of comment
                // Appel à validateToken
                //restTemplate.postForObject("http://localhost:9090/agd/security-service/validateToken", securityRequest, Void.class);
                HttpEntity<String> entity = new HttpEntity<>(headers);
                restTemplate.postForEntity(
                        "http://localhost:9090/agd/security-service/validateToken",
                        entity,
                        Void.class
                );
                return chain.filter(exchange);
            } else {
                return Mono.error(new RuntimeException("Missing or invalid authorization header"));
            }
        } else {
            return chain.filter(exchange);
        }
    }
    */



}