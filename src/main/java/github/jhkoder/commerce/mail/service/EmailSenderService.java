package github.jhkoder.commerce.mail.service;

import github.jhkoder.commerce.mail.config.EmailConfig;
import github.jhkoder.commerce.mail.service.request.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

@Configuration
public class EmailSenderService {
    private final JavaMailSender emailConfig;

    private final String email;

    public EmailSenderService(EmailConfig emailConfig,@Value("${spring.mail.username}")String email) {
        this.emailConfig = emailConfig.getJavaMailSender();
        this.email=email;
    }

    /**
     * @MailParseException – 메시지 구문 분석에 실패한 경우
     * @MailAuthenticationException – 인증이 실패한 경우
     * @MailSendException – 메시지 전송에 실패한 경우
     */
    public void sendMessage(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(request.getTo());
        message.setSubject(request.getSubject());
        message.setText(request.getText());
        emailConfig.send(message);
    }
}
