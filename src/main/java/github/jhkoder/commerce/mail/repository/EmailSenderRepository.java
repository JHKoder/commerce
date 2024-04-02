package github.jhkoder.commerce.mail.repository;

import github.jhkoder.commerce.mail.config.EmailConfig;
import github.jhkoder.commerce.mail.repository.request.EmailRequest;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class EmailSenderRepository {
    private final JavaMailSender emailConfig;

    public EmailSenderRepository(EmailConfig emailConfig) {
        this.emailConfig = emailConfig.getJavaMailSender();
    }

    /**
     * @MailParseException – 메시지 구문 분석에 실패한 경우
     * @MailAuthenticationException – 인증이 실패한 경우
     * @MailSendException – 메시지 전송에 실패한 경우
     */
    public void sendMessage(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("jhkoder.email@gmail.com");
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        emailConfig.send(message);
    }
}
