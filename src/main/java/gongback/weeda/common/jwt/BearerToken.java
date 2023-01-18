package gongback.weeda.common.jwt;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

@Getter
public class BearerToken extends AbstractAuthenticationToken {

    private final String jwt;

    public BearerToken(Collection<? extends GrantedAuthority> authorities, String jwt) {
        super(authorities);
        this.jwt = jwt;
    }

    @Override
    public Object getCredentials() {
        return jwt;
    }

    @Override
    public Object getPrincipal() {
        return jwt;
    }
}
