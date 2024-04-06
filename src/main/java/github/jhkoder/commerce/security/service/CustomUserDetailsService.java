package github.jhkoder.commerce.security.service;

import github.jhkoder.commerce.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CustomUserDetailsService  implements UserDetailsService {
    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
        System.out.println("CustomUserDetailsService @Service 시작 ");
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        var user = userRepository.findByUserId(username)
                .orElseThrow(() -> new UsernameNotFoundException("해당 유저를 찾을 수 없습니다. username: %s".formatted(username)));

        var roles = List.of(new SimpleGrantedAuthority(user.getRole().getAuthority()));

        System.out.println("custom user DetailService " + user.getUserId()+","+user.getPassword()+","+ roles);

        return new User(user.getUserId(), user.getPassword(), roles);
    }
}