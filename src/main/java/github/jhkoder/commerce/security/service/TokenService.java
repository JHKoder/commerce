package github.jhkoder.commerce.security.service;

import github.jhkoder.commerce.security.exception.JwtExpiredTokenException;
import github.jhkoder.commerce.security.rest.dto.JwtAccessTokenResponse;
import github.jhkoder.commerce.security.rest.dto.JwtRefreshTokenResponse;
import github.jhkoder.commerce.security.service.dto.TokenParserResponse;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.repository.UserRepository;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class TokenService {
    private static final String AUTHORITIES_KEY = "roles";

    private final SecretKey key;
    private final long expirationTime;
    private final long refreshTime;
    private final String issuer;
    private final UserRepository userRepository;

    public TokenService(@Value("${jwt.token.secret-key}") String key,
                        @Value("${jwt.token.expTime}") long expirationTime,
                        @Value("${jwt.token.refreshTime}") long refreshTime,
                        @Value("${jwt.token.issuer}") String issuer,
                        UserRepository userRepository) {
        this.key = Keys.hmacShaKeyFor(key.getBytes());
        this.expirationTime = expirationTime;
        this.refreshTime = refreshTime;
        this.issuer = issuer;
        this.userRepository=userRepository;
    }


    public String createRefreshToken(String username, Collection<GrantedAuthority> authorities) {
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiredAt = issuedAt.plusDays(refreshTime);

        return Jwts.builder()
                .addClaims(createClaims(username, authorities))
                .setIssuer(issuer)
                .setIssuedAt(toDate(issuedAt))
                .setExpiration(toDate(expiredAt))
                .signWith(key)
                .compact();
    }

    public String createToken(String username, Collection<GrantedAuthority> authorities) {
        LocalDateTime issuedAt = LocalDateTime.now();
        LocalDateTime expiredAt = issuedAt.plusMinutes(expirationTime);

        return Jwts.builder()
                .addClaims(createClaims(username, authorities))
                .setIssuer(issuer)
                .setIssuedAt(toDate(issuedAt))
                .setExpiration(toDate(expiredAt))
                .signWith(key)
                .compact();
    }
    public String createToken(String username, List<Role> authorities) {
        Collection<GrantedAuthority> grantedAuthorities = new ArrayList<>(authorities);
        return createToken(username,grantedAuthorities);
    }


    public JwtRefreshTokenResponse accessTokenRefresh(String accessToken) {
        TokenParserResponse response = this.parserBearerToken(accessToken);
        var token = createToken(response.username(),response.roles());
        return new JwtRefreshTokenResponse(token);
    }

    public JwtAccessTokenResponse findAccessToken(String accessToken) {
        System.out.println(accessToken);
        TokenParserResponse response = this.parserBearerToken(accessToken);
        System.out.println(response.username());
        System.out.println(response.roles());
        return new JwtAccessTokenResponse(response.username(),response.roles());
    }

    public TokenParserResponse parserToken(String token) throws BadCredentialsException, JwtExpiredTokenException {
        try {
            return tokenParserResponse(
                    Jwts.parserBuilder()
                            .setSigningKey(key)
                            .build()
                            .parseClaimsJws(token));
        } catch (SignatureException | UnsupportedJwtException | MalformedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("Invalid JWT token", ex);
        } catch (ExpiredJwtException expiredEx) {
            throw new JwtExpiredTokenException("JWT Token expired", expiredEx);
        }
    }

    public TokenParserResponse parserBearerToken(String token) throws BadCredentialsException, JwtExpiredTokenException {
        return parserToken(extractToken(token));
    }

    private String extractToken(String tokenPayload) {
        if (isNull(tokenPayload) || !tokenPayload.startsWith("Bearer ")) {
            return "";
        }
        return tokenPayload.replace("Bearer ", "");
    }


    @SuppressWarnings("unchecked")
    private TokenParserResponse tokenParserResponse(Jws<Claims> claimsJws) {
        String username = claimsJws.getBody().getSubject();
        List<String> roles = claimsJws.getBody().get(AUTHORITIES_KEY, List.class);

        return new TokenParserResponse(username, roles.stream().map(Role::of).toList());
    }

    private Claims createClaims(String username, Collection<GrantedAuthority> authorities) {
        Claims claims = Jwts.claims().setSubject(username);
        claims.put(AUTHORITIES_KEY, authorities.stream().map(Object::toString).toList());

        return claims;
    }

    private Date toDate(LocalDateTime dateTime) {
        return Date.from(dateTime.atZone(ZoneId.systemDefault()).toInstant());
    }
}