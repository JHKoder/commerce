package github.jhkoder.commerce.mail.repository;

import github.jhkoder.commerce.mail.repository.request.EmailRequest;

public interface EmailRepository {
    void sendMessage(EmailRequest emailRequest);
}
