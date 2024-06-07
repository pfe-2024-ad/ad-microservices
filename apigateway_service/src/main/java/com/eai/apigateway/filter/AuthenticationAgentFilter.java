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
public class AuthenticationAgentFilter extends AbstractGatewayFilterFactory<AuthenticationAgentFilter.Config> {

    @Autowired
    private WebClient.Builder webClientBuilder;

    public AuthenticationAgentFilter() {
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
                            if (role.equals("ROLE_AGENT")) {
                                log.info("User has agent role");
                                return chain.filter(exchange); // Proceed to next filter (AuthenticationFilter)
                            } else {
                                log.info("This URL is for agent only");
                                return Mono.error(new RuntimeException("Unauthorized: Not an agent"));
                            }
                        });

        };
    }

    public static class Config {
        // Configuration properties if needed
    }
}
