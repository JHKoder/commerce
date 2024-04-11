package github.jhkoder.commerce.common.auth;

import github.jhkoder.commerce.user.domain.Role;
import org.springframework.security.test.context.support.WithSecurityContext;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@WithSecurityContext(factory = WithUserDetailsSecurityContextFactory.class )
public @interface WithMockCustomUser {

    String username() default "userName";
    String password() default "password";
    Role[] role() default {Role.USER};
}