package github.jhkoder.commerce.security.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.jhkoder.commerce.security.handler.response.TokenResponse;
import github.jhkoder.commerce.security.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.WebAttributes;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

import static java.util.Objects.isNull;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtTokenIssueSuccessHandler implements AuthenticationSuccessHandler {
    private final ObjectMapper objectMapper;
    private final TokenService tokenService;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        log.info("AuthenticationSuccessHandler - ");
        onAuthenticationSuccess(request, response, (UsernamePasswordAuthenticationToken) authentication);
    }

    private void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                         UsernamePasswordAuthenticationToken authentication) throws IOException {

        String username = authentication.getPrincipal().toString();
        var authorities = authentication.getAuthorities();
        var token = tokenService.createToken(username, authorities);
        var refreshToken = tokenService.createRefreshToken(username, authorities);

        var tokenResponse = new TokenResponse(token,refreshToken);

        response.setStatus(HttpStatus.OK.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), tokenResponse);

        log.info("JWT 토큰 부여 :"+ username);
        var session = request.getSession(false);
        if (!isNull(session)) {
            session.removeAttribute(WebAttributes.AUTHENTICATION_EXCEPTION);
        }
    }
}