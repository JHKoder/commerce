package github.jhkoder.commerce.user.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import github.jhkoder.commerce.user.service.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;


    @Transactional
    public void signup(SignUpRequest request) {
        String encodePassword = passwordEncoder.encode(request.password());

        User user = new User(request.userId(), request.userName(), encodePassword, request.email(), request.phone(), request.gender(), Role.USER);

        userRepository.save(user);
    }

    @Transactional(readOnly = true)
    public boolean isIdCheck(String id) {
        if (userRepository.findByUserId(id).isEmpty()) {
            throw new ApiException(ErrorCode.USER_ID_DUPLICATE);
        }
        return true;
    }

    @Transactional(readOnly = true)
    public void validateMemberRegistration(SignUpRequest request) {
        if (userRepository.findByUserId(request.userId()).isPresent()) {
            throw new ApiException(ErrorCode.USER_ID_DUPLICATE);
        }
        if (request.authenticationType().equals(SignCertAuthentication.EMAIL) &&
                userRepository.findByEmail(request.email()).isPresent()) {
            throw new ApiException(ErrorCode.SIGNUP_EMAIL_DUPLICATE);
        } else if (request.authenticationType().equals(SignCertAuthentication.PHONE) &&
                userRepository.findByPhone(request.phone()).isPresent()) {
            throw new ApiException(ErrorCode.SIGNUP_SMS_DUPLICATE);
        }
    }

    @Transactional(readOnly = true)
    public void checkSmsValidAndUnique(String phone) {
        if (!userRepository.existsByPhone(phone)) {
            throw new ApiException(ErrorCode.USER_PHONE_UNIQUE);
        }
    }

    @Transactional(readOnly = true)
    public void checkEmailValidAndUnique(String email) {
        if (!userRepository.existsByEmail(email)) {
            throw new ApiException(ErrorCode.USER_EMAIL_UNIQUE);
        }
    }

}
