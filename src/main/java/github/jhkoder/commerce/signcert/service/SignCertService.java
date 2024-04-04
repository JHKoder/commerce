package github.jhkoder.commerce.signcert.service;

import github.jhkoder.commerce.common.entity.OracleBoolean;
import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.signcert.domain.SignCert;
import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;
import github.jhkoder.commerce.signcert.repository.SignCertDslRepository;
import github.jhkoder.commerce.signcert.repository.SignCertRepository;
import github.jhkoder.commerce.signcert.service.request.SignUpCertRequest;
import github.jhkoder.commerce.signcert.service.request.SignUpCertVerifyRequest;
import github.jhkoder.commerce.signcert.service.request.SignUpValidRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Random;

import static github.jhkoder.commerce.signcert.domain.SignCertAuthentication.EMAIL;
import static github.jhkoder.commerce.signcert.domain.SignCertAuthentication.PHONE;

@Service
@RequiredArgsConstructor
public class SignCertService {
    private final SignCertDslRepository dslRepository;
    private final SignCertRepository repository;

    @Transactional
    public void saveCert(SignUpCertRequest request) {
        SignCert signCert = new SignCert(request.verificationCode(), request.verificationSent(), request.signCertAuthentication(), OracleBoolean.F);
        repository.save(signCert);
    }

    @Transactional(readOnly = true)
    public void validateCert(SignUpValidRequest request) {
        boolean isAuthentication = repository.findBySignCertAuthenticationAndVerificationSentAndAuthentication(
                request.authenticationType(),
                request.authenticationType().equals(SignCertAuthentication.EMAIL) ? request.email() : request.phone(),
                OracleBoolean.T
        ).isPresent();

        if (!isAuthentication) {
            throw new ApiException(ErrorCode.SIGNUP_CERT_CODE_UNVERIFIED);
        }
    }

    public boolean smsVerifyCodeCheck(SignUpCertVerifyRequest request) {
        verifyCodeCheck(request, SignCertAuthentication.PHONE)
                .orElseThrow(() -> new ApiException(ErrorCode.SIGNUP_SMS_VERIFY_CODE_FAILED));
        updateSignCert(PHONE, request.send());
        return true;
    }

    public boolean emailVerifyCodeCheck(SignUpCertVerifyRequest request) {
        verifyCodeCheck(request, EMAIL)
                .orElseThrow(() -> new ApiException(ErrorCode.SIGNUP_EMAIL_VERIFY_CODE_FAILED));
        updateSignCert(EMAIL, request.send());
        return true;
    }

    public int newVerificationCode() {
        Random random = new Random();
        return 100000 + random.nextInt(900000);
    }

    @Transactional(readOnly = true)
    public void validateSmsVerificationExceed(String sms) {
        int count = dslRepository.countByVerificationSent(sms, SignCertAuthentication.PHONE);

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
    private void updateSignCert(SignCertAuthentication auth, String sent) {
        dslRepository.updateAuthenticationBySignCertAuthentication(auth, OracleBoolean.T, sent);
    }

    @Transactional(readOnly = true)
    private Optional<SignCert> verifyCodeCheck(SignUpCertVerifyRequest request, SignCertAuthentication signCertAuthentication) {
        return dslRepository.findByVerificationSentAndVerificationCode(request.send(), request.code(), signCertAuthentication);
    }


}
