package gongback.weeda.common.properties;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.ConstructorBinding;

@Getter
@ConstructorBinding
@ConfigurationProperties(prefix = "jwt")
public class JwtProperties {

    private String key;
    private String type;
    private String claimsKey;
    private Long expiredTimeMs;

    public JwtProperties(String key, String type, String claimsKey, Long expiredTimeMs) {
        this.key = key;
        this.type = type;
        this.claimsKey = claimsKey;
        this.expiredTimeMs = expiredTimeMs;
    }
}
