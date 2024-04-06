package github.jhkoder.commerce.security.provider;


import github.jhkoder.commerce.security.JwtAuthenticationToken;
import github.jhkoder.commerce.security.service.TokenService;
import github.jhkoder.commerce.security.service.response.TokenParserResponse;
import github.jhkoder.commerce.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtAuthenticationProvider implements AuthenticationProvider {
    private final TokenService tokenService;

    public JwtAuthenticationProvider(TokenService tokenService) {
        System.out.println("JwtAuthenticationProvider @Component 등록");
        this.tokenService = tokenService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("JwtAuthenticationProvider - authenticate() 호출");
        return authenticate((JwtAuthenticationToken) authentication);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (JwtAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private Authentication authenticate(JwtAuthenticationToken authentication) throws AuthenticationException {
        String jwtToken = authentication.getCredentials();
        TokenParserResponse response = tokenService.parserToken(jwtToken);

        return new JwtAuthenticationToken(response.username(), authorities(response));
    }

    private List<SimpleGrantedAuthority> authorities(TokenParserResponse response) {
        return response.roles().stream()
                .map(Role::value)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}