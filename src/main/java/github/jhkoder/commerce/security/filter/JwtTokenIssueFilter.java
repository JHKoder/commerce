package github.jhkoder.commerce.security.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.jhkoder.commerce.exception.AuthenticationCustomException;
import github.jhkoder.commerce.security.filter.request.LoginRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;

import static github.jhkoder.commerce.exception.ErrorCode.SECURITY_AUTHENTICATION_METHOD_NOT_SUPPORTED;

@Slf4j
public class JwtTokenIssueFilter extends AbstractAuthenticationProcessingFilter {
    private final ObjectMapper objectMapper;

    public JwtTokenIssueFilter(String defaultFilterProcessesUrl, ObjectMapper objectMapper,
                               AuthenticationSuccessHandler authenticationSuccessHandler,
                               AuthenticationFailureHandler authenticationFailureHandler) {
        super(defaultFilterProcessesUrl);
        this.objectMapper = objectMapper;
        this.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        this.setAuthenticationFailureHandler(authenticationFailureHandler);
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException, IOException {
        System.out.println("attemptAuthentication () ");
        if (!isPostMethod(request)) {
            throw new AuthenticationCustomException(SECURITY_AUTHENTICATION_METHOD_NOT_SUPPORTED);
        }

        var loginRequest = objectMapper.readValue(request.getReader(), LoginRequest.class);
        var token = UsernamePasswordAuthenticationToken.unauthenticated(loginRequest.username(), loginRequest.password());

        return this.getAuthenticationManager().authenticate(token);
    }

    private boolean isPostMethod(HttpServletRequest request) {
        return HttpMethod.POST.name().equals(request.getMethod());
    }
}