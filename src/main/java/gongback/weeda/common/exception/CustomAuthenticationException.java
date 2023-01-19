package gongback.weeda.common.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;

@Slf4j
public class CustomAuthenticationException extends AuthenticationException {
    public CustomAuthenticationException(String msg, Throwable cause) {
        super(msg, cause);
        log.error("[CustomAuthenticationException]", cause);
    }

    public CustomAuthenticationException(String msg) {
        super(msg);
    }
}
