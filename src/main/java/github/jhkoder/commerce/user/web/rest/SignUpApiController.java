package github.jhkoder.commerce.user.web.rest;

import github.jhkoder.commerce.user.service.LoginService;
import github.jhkoder.commerce.user.service.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class SignUpApiController {
    private final LoginService loginService;

    @PostMapping("/signup")
    public void signup(SignUpRequest request){

    }

}
