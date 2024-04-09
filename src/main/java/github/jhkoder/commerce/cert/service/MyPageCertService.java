package github.jhkoder.commerce.cert.service;

import github.jhkoder.commerce.cert.domain.Cert;
import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.cert.repository.CertDslRepository;
import github.jhkoder.commerce.cert.repository.CertRepository;
import github.jhkoder.commerce.cert.service.request.SignUpCertRequest;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.user.service.request.mypage.MyPageUserSendValidRequest;
import github.jhkoder.commerce.user.service.response.MyPageUpCertVerifyResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import static github.jhkoder.commerce.cert.domain.CertAuthentication.EMAIL;
import static github.jhkoder.commerce.cert.domain.CertAuthentication.PHONE;

@Service
@RequiredArgsConstructor
public class MyPageCertService extends CertService {
    private final CertDslRepository dslRepository;
    private final CertRepository repository;

    @Transactional
    public void saveCert(SignUpCertRequest request) {
        Cert cert = new Cert(request.verificationCode(), request.verificationSent(), request.certAuthentication(), OracleBoolean.F);
        repository.save(cert);
    }

    public MyPageUpCertVerifyResponse smsVerifyCodeCheck(MyPageUserSendValidRequest request) {
        verifyCodeCheck(request, CertAuthentication.PHONE)
                .orElseThrow(() -> new ApiException(ErrorCode.SIGNUP_SMS_VERIFY_CODE_FAILED));
        updateSignCert(PHONE, request.send());
        return new MyPageUpCertVerifyResponse(true);
    }

    public MyPageUpCertVerifyResponse emailVerifyCodeCheck(MyPageUserSendValidRequest request) {
        verifyCodeCheck(request, EMAIL)
                .orElseThrow(() -> new ApiException(ErrorCode.SIGNUP_EMAIL_VERIFY_CODE_FAILED));
        updateSignCert(EMAIL, request.send());
        return new MyPageUpCertVerifyResponse(true);
    }

    public int newVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    @Transactional(readOnly = true)
    public void validateSmsVerificationExceed(String sms) {
        int count = dslRepository.countByVerificationSent(sms, CertAuthentication.PHONE);

        if (count >= 5) {
            throw new ApiException(ErrorCode.SIGNUP_SMS_EXCEED);
        }
    }

    @Transactional(readOnly = true)
    public void validateEmailVerificationExceed(String email) {
        int count = dslRepository.countByVerificationSent(email, EMAIL);

        if (count >= 10) {
            throw new ApiException(ErrorCode.SIGNUP_EMAIL_EXCEED);
        }
    }


    @Transactional
    private void updateSignCert(CertAuthentication auth, String sent) {
        dslRepository.updateAuthenticationByCertAuthentication(auth, OracleBoolean.T, sent);
    }

    @Transactional(readOnly = true)
    private Optional<Cert> verifyCodeCheck(MyPageUserSendValidRequest request, CertAuthentication certAuthentication) {
        return dslRepository.findByVerificationSentAndVerificationCode(request.send(), request.code(), certAuthentication);
    }


    @Transactional
    public void deleteAuthenticationComplete(MyPageUserSendValidRequest request, CertAuthentication cert) {
        List<Cert> certs = repository.findByVerificationSentAndAuthentication(request.send(), OracleBoolean.T)
                .orElse(Collections.emptyList());
        repository.deleteAll(certs);

    }
}
