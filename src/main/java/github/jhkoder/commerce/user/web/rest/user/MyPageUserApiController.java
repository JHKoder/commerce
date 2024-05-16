package github.jhkoder.commerce.user.web.rest.user;


import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.cert.service.MyPageCertService;
import github.jhkoder.commerce.mail.service.EmailService;
import github.jhkoder.commerce.cert.service.request.SignUpCertRequest;
import github.jhkoder.commerce.security.dto.CurrentUser;
import github.jhkoder.commerce.sms.service.SmsService;
import github.jhkoder.commerce.user.domain.Role;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.service.UserAuthorizationApplicationService;
import github.jhkoder.commerce.user.service.UserService;
import github.jhkoder.commerce.user.service.request.mypage.MyPageUserEmailSendRequest;
import github.jhkoder.commerce.user.service.request.mypage.MyPageUserSendValidRequest;
import github.jhkoder.commerce.user.service.request.mypage.MyPageUserSmsSendRequest;
import github.jhkoder.commerce.user.service.request.mypage.UserPasswordChangeRequest;
import github.jhkoder.commerce.user.service.response.MyPageUpCertVerifyResponse;
import github.jhkoder.commerce.user.service.response.MyPageUserResponse;
import github.jhkoder.commerce.user.service.response.ValidEmailAndPhoneResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user/mypage")
@RequiredArgsConstructor
public class MyPageUserApiController {
    private final UserAuthorizationApplicationService uaaService;
    private final MyPageCertService certService;
    private final EmailService emailService;
    private final UserService userService;
    private final SmsService smsService;

    @PostMapping
    public MyPageUserResponse view(@AuthenticationPrincipal String username){
        return userService.findByMyPageUser(username);
    }

    @PostMapping("/role/seller")
    public void roleToSellerRequest(@AuthenticationPrincipal String username) {
        userService.validEmailAndPhone(username);
        userService.roleUpdate(username, Role.SELLER);
    }

    @PostMapping("/role/admin")
    public void roleToAdminRequest(@AuthenticationPrincipal String username) {
        User user = userService.validEmailAndPhone(username);
        uaaService.roleRequest(user, Role.ADMIN);
    }

    @PostMapping("/password")
    public void passwordChange(@AuthenticationPrincipal String username ,
                        @Valid @RequestBody UserPasswordChangeRequest request){
        userService.passwordChange(username,request);
    }

    @GetMapping("/auth/check")
    public ValidEmailAndPhoneResponse isEmailAndPhoneCheck(@AuthenticationPrincipal String username){
        return userService.validEmailAndPhoneCheck(username);
    }

    @PostMapping("/sms")
    public void smsCertCodeSend( @Valid @RequestBody MyPageUserSmsSendRequest request){
        userService.checkSmsValidAndUnique(request.phone());
        certService.validateSmsVerificationExceed(request.phone());
        int verificationCode = certService.newVerificationCode();
        smsService.signupCertSend(request.phone(), verificationCode);
        certService.saveCert(new SignUpCertRequest(verificationCode, request.phone(), CertAuthentication.PHONE));
    }

    @PostMapping("/email")
    public void emailCertCodeSend(@Valid @RequestBody MyPageUserEmailSendRequest request){
        userService.checkEmailValidAndUnique(request.email());
        certService.validateEmailVerificationExceed(request.email());
        int verificationCode = certService.newVerificationCode();
        emailService.certSend(request.email(), verificationCode);
        certService.saveCert(new SignUpCertRequest(verificationCode, request.email(), CertAuthentication.EMAIL));
    }

    @PatchMapping("/sms")
    public MyPageUpCertVerifyResponse smsCertVerify(@AuthenticationPrincipal String username  ,
                                                    @Valid @RequestBody MyPageUserSendValidRequest request){
        MyPageUpCertVerifyResponse response =certService.smsVerifyCodeCheck(request);
        certService.deleteAuthenticationComplete(request,CertAuthentication.PHONE);
        userService.updatePhone(username,request.send());
        return response;
    }

    @PatchMapping("/email")
    public MyPageUpCertVerifyResponse emailCertVerify(@AuthenticationPrincipal String username ,
                                                    @Valid @RequestBody MyPageUserSendValidRequest request){
        MyPageUpCertVerifyResponse response = certService.emailVerifyCodeCheck(request);
        certService.deleteAuthenticationComplete(request,CertAuthentication.EMAIL);
        userService.updateEmail(username,request.send());
        return response;
    }

}
