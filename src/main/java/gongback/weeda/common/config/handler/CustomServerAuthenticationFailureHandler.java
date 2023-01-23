package gongback.weeda.common.config.handler;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import gongback.weeda.api.controller.response.ApiResponse;
import gongback.weeda.common.exception.ResponseCode;
import org.springframework.core.io.buffer.DataBuffer;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.ServerAuthenticationFailureHandler;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class CustomServerAuthenticationFailureHandler implements ServerAuthenticationFailureHandler {
    private final ObjectMapper objectMapper;

    public CustomServerAuthenticationFailureHandler(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    @Override
    public Mono<Void> onAuthenticationFailure(WebFilterExchange webFilterExchange, AuthenticationException exception) {
        ServerHttpResponse response = webFilterExchange.getExchange().getResponse();

        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        response.getHeaders().add("Content-Type", "application/json");
        try {
            byte[] bytes = objectMapper.writeValueAsBytes(ApiResponse.of(ResponseCode.INVALID_TOKEN));
            DataBuffer body = response.bufferFactory().wrap(bytes);
            return response.writeWith(Mono.just(body));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}
