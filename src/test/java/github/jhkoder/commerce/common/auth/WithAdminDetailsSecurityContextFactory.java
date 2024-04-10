package github.jhkoder.commerce.common.auth;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

import java.util.Arrays;

public class WithAdminDetailsSecurityContextFactory implements WithSecurityContextFactory<WithMockCustomAdmin>{

    @Override
    public SecurityContext createSecurityContext(WithMockCustomAdmin annotation) {

        SecurityContext securityContext = SecurityContextHolder.createEmptyContext();

        UsernamePasswordAuthenticationToken authenticationToken
                = new UsernamePasswordAuthenticationToken(annotation.username(),
                "password",
                Arrays.asList(new SimpleGrantedAuthority(annotation.role().value())));

        securityContext.setAuthentication(authenticationToken);
        return securityContext;
    }
}
