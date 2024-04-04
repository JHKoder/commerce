package github.jhkoder.commerce.user.web.rest;

import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;
import github.jhkoder.commerce.signcert.service.SignCertService;
import github.jhkoder.commerce.signcert.service.request.SignUpCertRequest;
import github.jhkoder.commerce.signcert.service.request.SignUpCertVerifyRequest;
import github.jhkoder.commerce.signcert.service.request.SignUpValidRequest;
import github.jhkoder.commerce.sms.service.SmsService;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.request.SignUpRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/signup")
@RequiredArgsConstructor
public class SignUpApiController {
    private final SignCertService signCertService;
    private final UserService userService;
    private final SmsService smsService;
    private final EmailService emailService;

    /**
     * 1. 가입된 회원인지 재확인
     * 2. 인증된 [이메일,휴대폰] 재확인
     *
     * @param request
     */
    @PostMapping
    public void signup(@Valid @RequestBody SignUpRequest request) {
        signCertService.validateCert(SignUpValidRequest.of(request.email(), request.phone(), request.authenticationType()));
        userService.validateMemberRegistration(request);
        userService.signup(request);
    }

    @PostMapping("/idCheck")
    public boolean idCheck(@RequestBody String id) {
        return userService.isIdCheck(id);
    }

    /**
     * 회원가입 인증 번호 보내기
     * 1. 회원가입된 휴대폰 중복 검증
     * 2. 같은 인증은 하루 최대 5번으로 제한
     * 3. 휴대폰으로 인증번호 전달
     *
     * @param sms - 휴대폰 번호
     */
    @PostMapping("/cert/sms/send")
    public void smsCertCodeSend(String sms) {
        userService.checkSmsValidAndUnique(sms);
        signCertService.validateSmsVerificationExceed(sms);
        int verificationCode = signCertService.newVerificationCode();
        smsService.signupCertSend(sms, verificationCode);
        signCertService.saveCert(new SignUpCertRequest(verificationCode, sms, SignCertAuthentication.PHONE));
    }

    @PostMapping("/cert/email/send")
    public void emailCertCodeSend(String email) {
        userService.checkEmailValidAndUnique(email);
        signCertService.validateEmailVerificationExceed(email);
        int verificationCode = signCertService.newVerificationCode();
        emailService.signupCertSend(email, verificationCode);
        signCertService.saveCert(new SignUpCertRequest(verificationCode, email, SignCertAuthentication.EMAIL));
    }

    @PostMapping("/cert/sms/verify")
    public boolean smsCertVerify(@Valid @RequestBody SignUpCertVerifyRequest verifyRequest) {
        return signCertService.smsVerifyCodeCheck(verifyRequest);
    }

    @PostMapping("/cert/email/verify")
    public boolean emailCertVerify(@Valid @RequestBody SignUpCertVerifyRequest verifyRequest) {
        return signCertService.emailVerifyCodeCheck(verifyRequest);
    }
}
