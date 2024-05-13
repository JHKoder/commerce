package github.jhkoder.commerce.security.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import github.jhkoder.commerce.security.SkipPathRequestMatcher;
import github.jhkoder.commerce.security.filter.JwtTokenAuthenticationFilter;
import github.jhkoder.commerce.security.filter.JwtTokenIssueFilter;
import github.jhkoder.commerce.security.provider.JwtAuthenticationProvider;
import github.jhkoder.commerce.security.provider.JwtTokenIssueProvider;
import github.jhkoder.commerce.user.domain.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.thymeleaf.context.IExpressionContext;
import org.thymeleaf.spring6.expression.Fields;

import java.util.List;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String API_ROOT_URL = "/api/**";

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(security -> security.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(this::authorizeHttpRequests)
                .addFilterBefore(jwtTokenIssueFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenAuthenticationFilter(List.of(AUTHENTICATION_URL), authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .formLogin(login -> login.loginPage("/login").permitAll());
        return http.build();
    }


    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http, JwtTokenIssueProvider jwtTokenIssueProvider, JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(jwtTokenIssueProvider);
        return authenticationManagerBuilder.build();
    }

    private void authorizeHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configurer) {
        configurer
                .requestMatchers("/", "/home", "/signup", "/signup/api/**","/api/auth/login","/login","/products/**").permitAll()
                .requestMatchers("/docs/**", "/img/**", "/css/**", "/js/**","/*.png","favicon.ico", "/layout/**", "/fragment/**").permitAll()

                .requestMatchers("/api/all/**","/all/products/category").permitAll()
                .requestMatchers("/api/user/**").hasAnyRole(Role.USER.name(), Role.SELLER.name(), Role.ADMIN.name())
                .requestMatchers("/api/seller/**").hasAnyRole(Role.SELLER.name(),Role.ADMIN.name())
                .requestMatchers("/api/admin/**").hasAnyRole(Role.ADMIN.name())

                .requestMatchers("/api/token","/api/token/**").permitAll()

                // 상점 & 카테고리
                .requestMatchers("/shop","/shop/**").permitAll()
//                .requestMatchers("/shop/products/**/pay").hasAnyRole(Role.USER.name(), Role.SELLER.name(), Role.ADMIN.name())

                // 마이페이지
                .requestMatchers("/mypage/users/**/user").hasAnyRole(Role.USER.name())
                .requestMatchers("/mypage/users/**/seller").hasAnyRole(Role.SELLER.name())
                .requestMatchers("/mypage/users/**/admin").hasAnyRole( Role.ADMIN.name())

                // 판매자용 스토어 등록 페이지
                .requestMatchers("/store","/store/**").hasAnyRole(Role.SELLER.name())
        ;
    }

    private JwtTokenIssueFilter jwtTokenIssueFilter(AuthenticationManager authenticationManager) {
        var filter = new JwtTokenIssueFilter(AUTHENTICATION_URL, objectMapper, successHandler, failureHandler);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }

    private JwtTokenAuthenticationFilter jwtTokenAuthenticationFilter(List<String> pathsToSkip, AuthenticationManager authenticationManager) {
        var matcher = new SkipPathRequestMatcher(pathsToSkip, API_ROOT_URL);
        var filter = new JwtTokenAuthenticationFilter(matcher, failureHandler);
        filter.setAuthenticationManager(authenticationManager);

        return filter;
    }
}
