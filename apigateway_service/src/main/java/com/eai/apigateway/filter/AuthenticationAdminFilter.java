package com.eai.apigateway.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
@Slf4j
public class AuthenticationAdminFilter extends AbstractGatewayFilterFactory<AuthenticationAdminFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AuthenticationAdminFilter() {
        super(Config.class);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {

            String requestTokenHeader = exchange.getRequest().getHeaders().getFirst("Authorization");


                String url = "http://localhost:7777/agd/security-service/verifier-role";
                return webClientBuilder.build()
                        .post()
                        .uri(url)
                        .header(HttpHeaders.AUTHORIZATION, requestTokenHeader)
                        .retrieve()
                        .bodyToMono(String.class)
                        .flatMap(role -> {
                            if (role.equals("ROLE_ADMIN")) {
                                log.info("User has admin role");
                                return chain.filter(exchange); // Proceed to next filter (AuthenticationFilter)
                            } else {
                                log.info("This URL is for admin only");
                                return Mono.error(new RuntimeException("Unauthorized: Not an admin"));
                            }
                        });

        };
    }

    public static class Config {
        // Configuration properties if needed
    }
}
