package github.jhkoder.commerce.security.filter;

import github.jhkoder.commerce.security.service.dto.JwtAuthenticationToken;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static java.util.Objects.isNull;

@Slf4j
public class JwtTokenAuthenticationFilter extends AbstractAuthenticationProcessingFilter {
    public JwtTokenAuthenticationFilter(RequestMatcher matcher, AuthenticationFailureHandler failureHandler) {
        super(matcher);
        this.setAuthenticationFailureHandler(failureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
//        Optional<Cookie> token = Arrays.stream(request.getCookies()).filter(cookie -> cookie.getName().
//                equalsIgnoreCase("accessToken")).findFirst();
//
//        if(token.isPresent()){
//            String tokens = extractToken(token.get().getValue());
//            return getAuthenticationManager().authenticate(new JwtAuthenticationToken(tokens));
//        }

        String tokenPayload = extractToken(request.getHeader(HttpHeaders.AUTHORIZATION));
        return getAuthenticationManager().authenticate(new JwtAuthenticationToken(tokenPayload));
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response, FilterChain chain,
                                            Authentication authentication) throws IOException, ServletException {


        log.info("JwtTokenAuthenticationFilterJwtTokenAuthenticationFilter-         successfulAuthentication");
        SecurityContext context = SecurityContextHolder.createEmptyContext();
        context.setAuthentication(authentication);

        SecurityContextHolder.setContext(context);
        chain.doFilter(request, response);
    }

    @Override
    protected void unsuccessfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                              AuthenticationException authenticationException) throws IOException, ServletException {

        log.info("JwtTokenAuthenticationFilter-fail");
        SecurityContextHolder.clearContext();
        getFailureHandler().onAuthenticationFailure(request, response, authenticationException);
    }

    private String extractToken(String tokenPayload) {
        if (isNull(tokenPayload) || !tokenPayload.startsWith("Bearer ")) {
            throw new BadCredentialsException("Invalid token");
        }
        return tokenPayload.replace("Bearer ", "");
    }
}
