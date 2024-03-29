package github.jhkoder.commerce.user.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/login")
    public String getLoginPage() {
        return "login";
    }

    @GetMapping("/")
    public String getPage() {
        return "home";
    }

    @GetMapping("/hello")
    public String gethelloPage() {
        return "hello";
    }
}
