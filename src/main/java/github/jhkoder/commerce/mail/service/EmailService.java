package github.jhkoder.commerce.mail.service;

import github.jhkoder.commerce.mail.service.request.EmailRequest;

public interface EmailService {
    void sendMessage(EmailRequest emailRequest);

    void signupCertSend(String email, int verificationCode);
}
