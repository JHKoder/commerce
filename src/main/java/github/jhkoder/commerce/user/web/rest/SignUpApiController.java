package github.jhkoder.commerce.user.web.rest;

import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.cert.service.SignCertService;
import github.jhkoder.commerce.cert.service.request.SignUpCertRequest;
import github.jhkoder.commerce.cert.service.request.SignUpCertVerifyRequest;
import github.jhkoder.commerce.cert.service.request.SignUpValidRequest;
import github.jhkoder.commerce.sms.service.SmsService;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.request.signup.SignUpEmailSendRequest;
import github.jhkoder.commerce.user.service.request.signup.SignUpRequest;
import github.jhkoder.commerce.user.service.request.signup.SignUpSmsSendRequest;
import github.jhkoder.commerce.user.service.response.SignUpCertVerifyResponse;
import github.jhkoder.commerce.user.service.response.SignUpIdCheckResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/signup/api")
@RequiredArgsConstructor
public class SignUpApiController {
    private final SignCertService signCertService;
    private final UserService userService;
    private final SmsService smsService;
    private final EmailService emailService;

    /**
     * 1. 가입된 회원인지 재확인
     * 2. 인증된 [이메일,휴대폰] 재확인
     * 3. 회원가입 승인
     *
     * @param request
     */
    @PostMapping
    public void signup(@Valid @RequestBody SignUpRequest request) {
        signCertService.validateCert(SignUpValidRequest.of(request.email(), request.phone(), request.authenticationType()));
        userService.validateMemberRegistration(request);
        userService.signup(request);
        signCertService.deleteAuthenticationComplete(request);
    }

    @PostMapping("/idCheck")
    public SignUpIdCheckResponse idCheck(@RequestBody String id) {
        return userService.isIdCheck(id);
    }

    /**
     * 회원가입 인증 번호 보내기
     * 1. 회원가입된 휴대폰 중복 검증
     * 2. 같은 인증은 하루 최대 5번으로 제한
     * 3. 휴대폰으로 인증번호 전달
     * # 휴대폰 인증의 경우 하루 최대 5번 으로 제약
     * # 이메일 인증의 경우 하루 최대 10번 으로 제약
     */
    @PostMapping("/cert/sms/send")
    public void smsCertCodeSend(@Valid @RequestBody SignUpSmsSendRequest request) {
        userService.checkSmsValidAndUnique(request.sms());
        signCertService.validateSmsVerificationExceed(request.sms());
        int verificationCode = signCertService.newVerificationCode();
        smsService.signupCertSend(request.sms(), verificationCode);
        signCertService.saveCert(new SignUpCertRequest(verificationCode, request.sms(), CertAuthentication.PHONE));

    }

    @PostMapping("/cert/email/send")
    public void emailCertCodeSend(@Valid @RequestBody SignUpEmailSendRequest request) {
        userService.checkEmailValidAndUnique(request.email());
        signCertService.validateEmailVerificationExceed(request.email());
        int verificationCode = signCertService.newVerificationCode();
        emailService.signupCertSend(request.email(), verificationCode);
        signCertService.saveCert(new SignUpCertRequest(verificationCode, request.email(), CertAuthentication.EMAIL));
    }

    @PostMapping("/cert/sms/verify")
    public SignUpCertVerifyResponse smsCertVerify(@Valid @RequestBody SignUpCertVerifyRequest verifyRequest) {
        return signCertService.smsVerifyCodeCheck(verifyRequest);
    }

    @PostMapping("/cert/email/verify")
    public SignUpCertVerifyResponse emailCertVerify(@Valid @RequestBody SignUpCertVerifyRequest verifyRequest) {
        return signCertService.emailVerifyCodeCheck(verifyRequest);
    }
}
