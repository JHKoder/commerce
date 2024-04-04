package github.jhkoder.commerce.mail.service.request;


public record EmailRequest(String to, String subject, String text) {
}
