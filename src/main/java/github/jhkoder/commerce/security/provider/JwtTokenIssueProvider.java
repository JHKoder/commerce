package github.jhkoder.commerce.security.provider;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class JwtTokenIssueProvider implements AuthenticationProvider {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;

    public JwtTokenIssueProvider(PasswordEncoder passwordEncoder, UserDetailsService userDetailsService) {

        System.out.println("JwtTokenIssueProvider @Component 등록");
        this.passwordEncoder = passwordEncoder;
        this.userDetailsService = userDetailsService;
    }

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        System.out.println("---authenticate");
        var username = (String) authentication.getPrincipal();
        var password = (String) authentication.getCredentials();

        System.out.println(username+","+password);
        return authenticate(username, password);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (UsernamePasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }

    private UsernamePasswordAuthenticationToken authenticate(String username, String password) {
        System.out.println("user service authenticate start  UserDetails");
        UserDetails user = userDetailsService.loadUserByUsername(username);
        System.out.println("user service authenticate end   ");
        System.out.println(password +" , "+ user.getPassword());
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("인증 실패. username or password 불일치");
        }

        return UsernamePasswordAuthenticationToken.authenticated(user.getUsername(), null, authorities(user));
    }

    private List<SimpleGrantedAuthority> authorities(UserDetails user) {
        return user.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .map(SimpleGrantedAuthority::new)
                .toList();
    }
}