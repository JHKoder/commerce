package github.jhkoder.commerce.common.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithSellerDetailsSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomSeller>{

    @Override
    public SecurityContext createSecurityContext(WithMockCustomSeller user) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetailsCustom principal = new UserDetailsCustom(user.username(), user.password(), user.role());

        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, "password", principal.getAuthorities());
        context.setAuthentication(auth);

        return context;
    }
}
