package github.jhkoder.commerce.cert.service.request;

import github.jhkoder.commerce.cert.domain.CertAuthentication;

public record SignUpValidRequest(String email, String phone, CertAuthentication authenticationType) {

    public static SignUpValidRequest of(String email, String phone, CertAuthentication certAuthentication) {
        return new SignUpValidRequest(email,phone, certAuthentication);
    }
}
