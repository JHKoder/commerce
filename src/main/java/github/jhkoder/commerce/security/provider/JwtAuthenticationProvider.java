package github.jhkoder.commerce.security.provider;

import github.jhkoder.commerce.security.service.TokenService;
import github.jhkoder.commerce.security.service.dto.JwtAuthenticationToken;
import github.jhkoder.commerce.security.service.dto.TokenParserResponse;
import github.jhkoder.commerce.user.domain.Role;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final TokenService tokenService;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        return authenticate((JwtAuthenticationToken) authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        log.info("JwtAuthenticationProvider  -  supports");
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication authenticate(JwtAuthenticationToken authentication) throws AuthenticationException {
        log.info("jwt auth authenticate");
        String jwtToken = authentication.getCredentials();
        TokenParserResponse response = tokenService.parserToken(jwtToken);
        log.info(jwtToken);

        return new JwtAuthenticationToken(response.username(), authorities(response));
    }

    private List<SimpleGrantedAuthority> authorities(TokenParserResponse response) {
        return response.roles().stream()
                .map(Role::value)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}
