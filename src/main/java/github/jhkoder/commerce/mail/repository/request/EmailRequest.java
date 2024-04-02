package github.jhkoder.commerce.mail.repository.request;

import lombok.Getter;

@Getter
public class EmailRequest {
    private final String to;
    private final String subject;
    private final String text;

    public EmailRequest(String to, String subject, String text) {
        this.to = to;
        this.subject = subject;
        this.text = text;
    }
}
