package github.jhkoder.commerce.mail.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.mail.config.EmailConfig;
import github.jhkoder.commerce.mail.service.request.EmailRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.MailAuthenticationException;
import org.springframework.mail.MailParseException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import static github.jhkoder.commerce.mail.output.EmailSignupOutput.*;

@Configuration
public class EmailSenderService implements EmailService {
    private final JavaMailSender emailConfig;

    private final String email;

    public EmailSenderService(EmailConfig emailConfig,@Value("${spring.mail.username}")String email) {
        this.emailConfig = emailConfig.getJavaMailSender();
        this.email=email;
    }

    public void sendMessage(EmailRequest request) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(email);
        message.setTo(request.to());
        message.setSubject(request.subject());
        message.setText(request.text());
        emailConfig.send(message);
    }

    @Override
    public void signupCertSend(String email, int verificationCode) {
        try {
            String title = signupTitle();
            String sendText = signupText(verificationCode);
            sendMessage(new EmailRequest(email, title, sendText));
        }catch (MailParseException e1){
            throw new ApiException(ErrorCode.EMAIL_SEND_PARSE);
        }catch (MailAuthenticationException e2){
            throw new ApiException(ErrorCode.EMAIL_SEND_AUTHENTICATION);
        }catch (MailSendException e3){
            throw new ApiException(ErrorCode.EMAIL_SEND);
        }
    }
}
