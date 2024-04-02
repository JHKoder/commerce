package github.jhkoder.commerce.mail.service;

import github.jhkoder.commerce.mail.service.request.EmailRequest;

public interface EmailRepository {
    void sendMessage(EmailRequest emailRequest);
}
