package github.jhkoder.commerce.user.web.view;

import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.Collection;
@Slf4j
@Controller
public class HomeController {

    @GetMapping("/login")
    public String getLoginPage() {

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authName = auth.getName();
        Object authPri = auth.getPrincipal();
        Collection<? extends GrantedAuthority> gren = auth.getAuthorities();

        log.info("[Security ] init count : "+SecurityContextHolder.getInitializeCount());
        log.info("[Security ] ContextHolderStrategy : "+SecurityContextHolder.getContextHolderStrategy());
        log.info("[Security ] Name in context holder: "+authName);
        log.info("[Security ] principal in context holder: "+authPri);
        log.info("[Security ] gren in context holder: "+gren);

        return "login";
    }

    @GetMapping("/")
    public String getPage() {


        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String authName = auth.getName();
        Object authPri = auth.getPrincipal();
        Collection<? extends GrantedAuthority> gren = auth.getAuthorities();

        log.info("[Security ] init count : "+SecurityContextHolder.getInitializeCount());
        log.info("[Security ] ContextHolderStrategy : "+SecurityContextHolder.getContextHolderStrategy());
        log.info("[Security ] Name in context holder: "+authName);
        log.info("[Security ] principal in context holder: "+authPri);
        log.info("[Security ] gren in context holder: "+gren);

        return "home";
    }

    @GetMapping("/hello")
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
