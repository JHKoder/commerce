package github.jhkoder.commerce.mail.service.request;

import lombok.Getter;

@Getter
public record EmailRequest(String to, String subject, String text) {
}
