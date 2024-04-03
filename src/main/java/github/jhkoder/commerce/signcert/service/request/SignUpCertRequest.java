package github.jhkoder.commerce.signcert.service.request;

import github.jhkoder.commerce.signcert.domain.SignUpCert;

public record SignUpCertRequest(String sessionId, String verificationCode, String verificationSent, SignUpCert signUpCert) {
}
