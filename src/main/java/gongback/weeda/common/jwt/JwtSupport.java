package gongback.weeda.common.jwt;

import gongback.weeda.common.exception.ResponseCode;
import gongback.weeda.common.exception.WeedaApplicationException;
import gongback.weeda.common.properties.JwtProperties;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;
import java.util.List;

@Slf4j
@Component
public class JwtSupport {

    private final JwtProperties jwtProperties;

    private final String CLAIMS_KEY;

    public JwtSupport(JwtProperties jwtProperties) {
        this.jwtProperties = jwtProperties;
        this.CLAIMS_KEY = jwtProperties.getClaimsKey();
    }

    public BearerToken generateJwt(String email, List<String> roles) {
        Claims claims = Jwts.claims();
        claims.put(CLAIMS_KEY, email);

        String jwt = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + jwtProperties.getExpiredTimeMs()))
                .signWith(getKey(), SignatureAlgorithm.HS256)
                .compact();

        return new BearerToken(
//                roles.stream().map(role -> new SimpleGrantedAuthority(role)).collect(Collectors.toList()),
                List.of(new SimpleGrantedAuthority("ROLE_USER")),
                jwt
        );
    }

    public boolean isValid(String username, UserDetails userDetails) {
        return username.equals(userDetails.getUsername());
    }

    public String getUsername(BearerToken token) {
        Claims claims = extractClaims(token);
        return claims.get(CLAIMS_KEY, String.class);
    }

    private Claims extractClaims(BearerToken token) {
        try {
            return Jwts.parserBuilder()
                    .setSigningKey(getKey())
                    .build().parseClaimsJws(token.getJwt()).getBody();
        } catch (MalformedJwtException | ExpiredJwtException e) {
            throw new WeedaApplicationException(ResponseCode.INVALID_TOKEN);
        }
    }

    private Key getKey() {
        byte[] keyBytes = jwtProperties.getKey().getBytes(StandardCharsets.UTF_8);
        return Keys.hmacShaKeyFor(keyBytes);
    }

}
