package github.jhkoder.commerce.user.web.rest;

import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;
import github.jhkoder.commerce.signcert.service.SignCertService;
import github.jhkoder.commerce.signcert.service.request.SignUpCertRequest;
import github.jhkoder.commerce.signcert.service.request.SignUpCertVerifyRequest;
import github.jhkoder.commerce.sms.service.SmsService;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.request.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController("/api/signup")
@RequiredArgsConstructor
public class SignUpApiController {
    private final SignCertService signCertService;
    private final UserService userService;
    private final SmsService smsService;
    private final EmailService emailService;


    @PostMapping
    public void signup(SignUpRequest request) {
        userService.validateMemberRegistration(request);
        signCertService.validateCert(request);
        userService.signup(request);
    }

    @PostMapping("/idCheck")
    public boolean idCheck(String id) {
        return userService.isIdCheck(id);
    }

    @PostMapping("/cert/sms/send")
    public void authSmsCertCodeSend(String sms) {
        userService.isSmsValidAndUnique(sms);
        int verificationCode = signCertService.newVerificationCode();
        smsService.signSend(sms,verificationCode);
    }

    @PostMapping("/cert/email/send")
    public void authEmailCertCodeSend(String email) {
        userService.isEmailValidAndUnique(email);
        int verificationCode = signCertService.newVerificationCode();
        emailService.signupCertSend(email,verificationCode);
        signCertService.saveCert(new SignUpCertRequest(verificationCode,email, SignCertAuthentication.EMAIL));
    }

    @PostMapping("/cert/sms/verify")
    public boolean authSmsCert(SignUpCertVerifyRequest verifyRequest) {
        return signCertService.smsVerifyCodeCheck(verifyRequest);
    }

    @PostMapping("/cert/email/verify")
    public boolean authEmailCert(SignUpCertVerifyRequest verifyRequest) {
        return signCertService.emailVerifyCodeCheck(verifyRequest);
    }
}
