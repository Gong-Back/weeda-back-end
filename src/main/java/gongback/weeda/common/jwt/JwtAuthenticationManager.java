package gongback.weeda.common.jwt;

import gongback.weeda.common.exception.CustomAuthenticationException;
import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.service.CustomUserDetailsService;
import org.springframework.security.authentication.ReactiveAuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthenticationManager implements ReactiveAuthenticationManager {

    private final JwtSupport jwtSupport;
    private final CustomUserDetailsService detailsService;

    public JwtAuthenticationManager(JwtSupport jwtSupport, CustomUserDetailsService detailsService) {
        this.jwtSupport = jwtSupport;
        this.detailsService = detailsService;
    }

    @Override
    public Mono<Authentication> authenticate(Authentication authentication) {
        return Mono.justOrEmpty(authentication)
                .filter(auth -> auth instanceof BearerToken)
                .cast(BearerToken.class)
                .flatMap(it -> validate(it))
                .onErrorMap(it -> new CustomAuthenticationException(it.getMessage()));
    }

    private Mono<Authentication> validate(BearerToken token) {
        String username = jwtSupport.getUsername(token);
        return detailsService.findByUsername(username)
                .filter(it -> jwtSupport.isValid(username, it))
                .switchIfEmpty(Mono.error(new WeedaApplicationException(ResponseCode.INVALID_TOKEN)))
                .map(it -> new UsernamePasswordAuthenticationToken(it.getUsername(), it.getPassword(), it.getAuthorities()));
    }

}
