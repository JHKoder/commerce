package github.jhkoder.commerce.signcert.service.request;

import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;

public record SignUpCertRequest(int verificationCode, String verificationSent, SignCertAuthentication signCertAuthentication) {
}
