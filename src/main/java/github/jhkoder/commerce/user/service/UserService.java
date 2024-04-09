package github.jhkoder.commerce.user.service;

import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import github.jhkoder.commerce.user.service.request.mypage.UserPasswordChangeRequest;
import github.jhkoder.commerce.user.service.request.signup.SignUpRequest;
import github.jhkoder.commerce.user.service.response.MyPageUserResponse;
import github.jhkoder.commerce.user.service.response.SignUpIdCheckResponse;
import github.jhkoder.commerce.user.service.response.ValidEmailAndPhoneResponse;
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
    public SignUpIdCheckResponse isIdCheck(String id) {
        if (userRepository.findByUserId(id).isPresent()) {
            throw new ApiException(ErrorCode.USER_ID_DUPLICATE);
        }
        return new SignUpIdCheckResponse(true);
    }

    @Transactional(readOnly = true)
    public void validateMemberRegistration(SignUpRequest request) {
        if (userRepository.findByUserId(request.userId()).isPresent()) {
            throw new ApiException(ErrorCode.USER_ID_DUPLICATE);
        }
        if (request.authenticationType().equals(CertAuthentication.EMAIL) &&
                userRepository.findByEmail(request.email()).isPresent()) {
            throw new ApiException(ErrorCode.SIGNUP_EMAIL_DUPLICATE);
        } else if (request.authenticationType().equals(CertAuthentication.PHONE) &&
                userRepository.findByPhone(request.phone()).isPresent()) {
            throw new ApiException(ErrorCode.SIGNUP_SMS_DUPLICATE);
        }
    }

    @Transactional(readOnly = true)
    public void checkSmsValidAndUnique(String phone) {
        if (userRepository.existsByPhone(phone)) {
            throw new ApiException(ErrorCode.USER_PHONE_UNIQUE);
        }
    }

    @Transactional(readOnly = true)
    public void checkEmailValidAndUnique(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new ApiException(ErrorCode.USER_EMAIL_UNIQUE);
        }
    }


    @Transactional
    public void roleUpdate(String userName, Role role) {
        userRepository.findByUserId(userName).
                orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND))
                .roleUpdate(role);
    }

    public User validEmailAndPhone(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        if (user.getEmail() == null) {
            throw new ApiException(ErrorCode.USER_NOT_EMAIL);
        }
        if (user.getPhone() == null) {
            throw new ApiException(ErrorCode.USER_NOT_PHONE);
        }
        return user;
    }

    @Transactional(readOnly = true)
    public ValidEmailAndPhoneResponse validEmailAndPhoneCheck(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));

        boolean isEmailValid = user.getEmail() != null;
        boolean isPhoneValid = user.getPhone() != null;

        return new ValidEmailAndPhoneResponse(isEmailValid, isPhoneValid);
    }

    @Transactional
    public void passwordChange(String userId, UserPasswordChangeRequest request) {
        String encodePassword = passwordEncoder.encode(request.password());
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
        user.updatePassword(encodePassword);
    }

    public void updatePhone(String userId, String send) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND))
                .updatePhone(send);

    }

    public void updateEmail(String userId, String send) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND))
                .updateEmail(send);
    }

    public MyPageUserResponse findByMyPageUser(String username) {
        return userRepository.findByUserId(username)
                .map(MyPageUserResponse::of)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }
}