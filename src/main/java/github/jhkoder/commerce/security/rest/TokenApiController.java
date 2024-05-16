package github.jhkoder.commerce.security.rest;

import github.jhkoder.commerce.security.rest.dto.JwtAccessTokenResponse;
import github.jhkoder.commerce.security.rest.dto.JwtRefreshTokenResponse;
import github.jhkoder.commerce.security.service.TokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/all/api/token")
@RequiredArgsConstructor
public class TokenApiController {
    private final TokenService tokenService;

    @PostMapping("/access")
    public JwtAccessTokenResponse accessLogin(@RequestHeader("Authorization") String accessToken){
        return tokenService.findAccessToken(accessToken);
    }

    @PostMapping("/refresh")
    public JwtRefreshTokenResponse isValidToken(@RequestHeader("Authorization") String accessToken){
        return tokenService.accessTokenRefresh(accessToken);
    }
}
