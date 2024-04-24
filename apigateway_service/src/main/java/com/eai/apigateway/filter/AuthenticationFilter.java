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

                    return Mono.error(new RuntimeException("missing authorization header"));
                }

                String requestTokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");



                if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                    String jwtToken = requestTokenHeader.substring(7);

                    HttpHeaders headers = new HttpHeaders();
                    headers.setBearerAuth(jwtToken);


                    HttpEntity<String> entity = new HttpEntity<>(headers);
                    restTemplate.postForEntity(
                            "http://localhost:7777/agd/security-service/validateTokenService",
                            entity,
                            Void.class
                    );

                    return chain.filter(exchange);
                }


            return chain.filter(exchange);
        });
    }


    public static class Config {

    }



}