package github.jhkoder.commerce.cert.service.request;

import github.jhkoder.commerce.cert.domain.CertAuthentication;

public record SignUpCertRequest(int verificationCode, String verificationSent, CertAuthentication certAuthentication) {
}
