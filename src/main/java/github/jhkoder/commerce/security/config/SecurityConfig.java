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
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer;
import org.springframework.security.config.annotation.web.configurers.FormLoginConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.util.List;

@Configuration
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    public static final String AUTHENTICATION_URL = "/api/auth/login";
    public static final String API_ROOT_URL = "/api/dev/**";

    private final AuthenticationSuccessHandler successHandler;
    private final AuthenticationFailureHandler failureHandler;

    private final ObjectMapper objectMapper;


    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, AuthenticationManager authenticationManager) throws Exception {
        System.out.println("config" + authenticationManager.getClass().getPackageName());
        return http
                .sessionManagement(configurer -> configurer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(this::authorizeHttpRequests)
                .formLogin(this::formLogin)
                .addFilterBefore(jwtTokenIssueFilter(authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(jwtTokenAuthenticationFilter(List.of(AUTHENTICATION_URL), authenticationManager), UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authenticationManager(HttpSecurity http,JwtTokenIssueProvider jwtTokenIssueProvider, JwtAuthenticationProvider jwtAuthenticationProvider) throws Exception {
        System.out.println("AuthenticationManager @Bean 등록 ");
        var authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
        authenticationManagerBuilder.authenticationProvider(jwtAuthenticationProvider);
        authenticationManagerBuilder.authenticationProvider(jwtTokenIssueProvider);
        return authenticationManagerBuilder.build();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    private void authorizeHttpRequests(AuthorizeHttpRequestsConfigurer<HttpSecurity>.AuthorizationManagerRequestMatcherRegistry configurer) {
        configurer
                .requestMatchers("/","/home","/signup").permitAll()
                .requestMatchers("/api/signup").permitAll()
                .requestMatchers( "/docs/**","/img/**","/css/**","/js/**","/layout/**","/fragment/**").permitAll()
                .requestMatchers("/api/dev/admin").hasAnyRole(Role.ADMIN.name())
                .requestMatchers("/api/dev/user").hasAnyRole(Role.USER.name())
                .anyRequest().authenticated();
    }

    private void formLogin(FormLoginConfigurer<HttpSecurity> httpSecurityFormLoginConfigurer) {
        httpSecurityFormLoginConfigurer
                .loginPage("/login") .permitAll();
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
