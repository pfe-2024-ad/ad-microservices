package com.eai.apigateway.filter;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class AuthenticationFilter extends AbstractGatewayFilterFactory<AuthenticationFilter.Config> {

    @Autowired
    private RestTemplate restTemplate;


    public AuthenticationFilter() {
        super(Config.class);
    }


    @Override
    public GatewayFilter apply(Config config) {
        log.info("This log is being logged regularly with no action.");
        return ((exchange, chain) -> {

            //header contains token or not
            if (!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
                return Mono.error(new RuntimeException("missing authorization header"));
            }


            String requestTokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");


            if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
                log.info("requestTokenHeader not null");

                return WebClient.create().post()
                        .uri("http://localhost:7777/agd/security-service/validateTokenService")
                        .header("Authorization", requestTokenHeader)
                        .exchange()
                        .flatMap(response -> {
                            if (response.statusCode().is2xxSuccessful()) {
                                log.info("auth good");
                                return Mono.empty(); // Return an empty Mono to satisfy the return type
                            } else {
                                log.info("auth bad");
                                return Mono.error(new RuntimeException("Authentication failed")); // Return an error Mono in case of authentication failure
                            }
                        })
                        .then(chain.filter(exchange));
            }

             return chain.filter(exchange);
        });
    }



    @Getter
    public static class Config {
        private String baseMessage;
        private boolean preLogger;
        private boolean postLogger;


    }


}