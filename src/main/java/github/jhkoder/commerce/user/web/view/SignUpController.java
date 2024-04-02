package github.jhkoder.commerce.user.web.view;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SignUpController {

    @GetMapping("/signup")
    public String signup(){
        return "signup";
    }
}
