package github.jhkoder.commerce.user.web.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Slf4j

@Controller
public class UserController {

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }

    @GetMapping("/mypage")
    public String mypage(){
        return "mypage";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/")
    public String getPage() {
        return "home";
    }

    @GetMapping("/user/hello")
    public String getHelloPage() {
        return "hello";
    }
    @GetMapping("/dashboard")
    public String getDashboardPage() {
        return "dashboard";
    }

    @GetMapping("/api/dev")
    public String getDashbdoardPage() {
        return "dashboard";
    }

}
