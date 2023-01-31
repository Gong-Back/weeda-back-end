package gongback.weeda.common.config.converter;

import gongback.weeda.common.jwt.BearerToken;
import gongback.weeda.common.properties.JwtProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.server.authentication.ServerAuthenticationConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationConverter implements ServerAuthenticationConverter {

    private final JwtProperties jwtProperties;

    public JwtAuthenticationConverter(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
    }

    @Override
    public Mono<Authentication> convert(ServerWebExchange exchange) {
        return Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION))
                .filter(token -> token.startsWith(jwtProperties.getType()))
                .map(token -> token.substring(7))
                .map(token -> new BearerToken(token));
    }
}
