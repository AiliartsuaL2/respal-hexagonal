package hckt.respalhex.auth.application.port.service;

import hckt.respalhex.auth.application.port.service.provider.CreateTokenProvider;
import hckt.respalhex.auth.application.port.service.provider.GetTokenInfoProvider;
import hckt.respalhex.auth.exception.ErrorMessage;
import hckt.respalhex.auth.exception.InvalidTokenException;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.servlet.http.HttpServletRequest;
import java.util.Base64;
import java.util.Date;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenProvider implements CreateTokenProvider, GetTokenInfoProvider {
    private final String secretKey;
    private final long accessTokenValidTime;
    private final long refreshTokenValidTime;
    private final UserDetailsService userDetailsService;

    public JwtTokenProvider(@Value("${jwt.secret-key}") String secretKey, @Value("${jwt.expired-time.access-token}") long accessTokenValidTime, @Value("${jwt.expired-time.refresh-token}") long refreshTokenValidTime, UserDetailsService userDetailsService) {
        this.secretKey = Base64.getEncoder().encodeToString(secretKey.getBytes());
        this.accessTokenValidTime = accessTokenValidTime;
        this.refreshTokenValidTime = refreshTokenValidTime;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public String createAccessToken(String payload) {
        return createToken(payload, accessTokenValidTime);
    }

    @Override
    public String createRefreshToken(String payload) {
        return createToken(payload, refreshTokenValidTime);
    }

    private String createToken(final String payload, final Long validityInMilliseconds) {
        Date now = new Date();
        Date validity = new Date(now.getTime() + validityInMilliseconds);

        return Jwts.builder()
                .setSubject(payload)
                .setIssuedAt(now)
                .setExpiration(validity)
                .signWith(SignatureAlgorithm.HS256, secretKey)
                .compact();
    }

    @Override
    public String getPayload(String token) {
        return Jwts.parser()
                .setSigningKey(secretKey)
                .parseClaimsJws(token)
                .getBody()
                .getSubject();
    }

    @Override
    public boolean isValid(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException ex) {
            return false;
        }
    }

    @Override
    public void validateAndThrow(String token) {
        try {
            Jws<Claims> claims = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            claims.getBody()
                    .getExpiration()
                    .before(new Date());
        } catch (JwtException ex) {
            throw new InvalidTokenException(ErrorMessage.INVALID_TOKEN_EXCEPTION.getMessage());
        }
    }

    @Override
    public Authentication getAuthentication(String accessToken) {
        UserDetails userDetails = userDetailsService.loadUserByUsername(getPayload(accessToken));
        return new UsernamePasswordAuthenticationToken(userDetails, "", userDetails.getAuthorities());
    }

    @Override
    public String resolveToken(HttpServletRequest request) {
        return request.getHeader("Authorization");
    }
}
