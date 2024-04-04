package github.jhkoder.commerce.signcert.service.request;

import github.jhkoder.commerce.signcert.domain.SignCertAuthentication;

public record SignUpValidRequest(String email, String phone, SignCertAuthentication authenticationType) {

    public static SignUpValidRequest of(String email, String phone, SignCertAuthentication signCertAuthentication) {
        return new SignUpValidRequest(email,phone,signCertAuthentication);
    }
}
