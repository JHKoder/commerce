package github.jhkoder.commerce.cert.service;

import github.jhkoder.commerce.cert.domain.Cert;
import github.jhkoder.commerce.cert.domain.CertAuthentication;
import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.cert.repository.CertDslRepository;
import github.jhkoder.commerce.cert.repository.CertRepository;
import github.jhkoder.commerce.cert.service.request.SignUpCertRequest;
import github.jhkoder.commerce.cert.service.request.SignUpCertVerifyRequest;
import github.jhkoder.commerce.cert.service.request.SignUpValidRequest;
import github.jhkoder.commerce.user.service.request.signup.SignUpRequest;
import github.jhkoder.commerce.user.service.response.SignUpCertVerifyResponse;
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
public class SignCertService extends CertService {
    private final CertDslRepository dslRepository;
    private final CertRepository repository;

    @Transactional
    public void saveCert(SignUpCertRequest request) {
        Cert cert = new Cert(request.verificationCode(), request.verificationSent(), request.certAuthentication(), OracleBoolean.F);
        repository.save(cert);
    }

    @Transactional(readOnly = true)
    public void validateCert(SignUpValidRequest request) {
        boolean isAuthentication = repository.findByCertAuthenticationAndVerificationSentAndAuthentication(
                request.authenticationType(),
                request.authenticationType().equals(CertAuthentication.EMAIL) ? request.email() : request.phone(),
                OracleBoolean.T
        ).isPresent();

        if (!isAuthentication) {
            throw new ApiException(ErrorCode.SIGNUP_CERT_CODE_UNVERIFIED);
        }
    }

    public SignUpCertVerifyResponse smsVerifyCodeCheck(SignUpCertVerifyRequest request) {
        verifyCodeCheck(request, CertAuthentication.PHONE)
                .orElseThrow(() -> new ApiException(ErrorCode.SIGNUP_SMS_VERIFY_CODE_FAILED));
        updateSignCert(PHONE, request.send());
        return new SignUpCertVerifyResponse(true);
    }

    public SignUpCertVerifyResponse emailVerifyCodeCheck(SignUpCertVerifyRequest request) {
        verifyCodeCheck(request, EMAIL)
                .orElseThrow(() -> new ApiException(ErrorCode.SIGNUP_EMAIL_VERIFY_CODE_FAILED));
        updateSignCert(EMAIL, request.send());
        return new SignUpCertVerifyResponse(true);
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
    private Optional<Cert> verifyCodeCheck(SignUpCertVerifyRequest request, CertAuthentication certAuthentication) {
        return dslRepository.findByVerificationSentAndVerificationCode(request.send(), request.code(), certAuthentication);
    }


    @Transactional
    public void deleteAuthenticationComplete(SignUpRequest request) {
        List<Cert> certs= repository.findByVerificationSentAndAuthentication(getSend(request),OracleBoolean.T)
                        .orElse(Collections.emptyList());
        repository.deleteAll(certs);

    }

    private String getSend(SignUpRequest request){
        return request.authenticationType().equals(EMAIL) ? request.email() : request.phone();
    }
}
