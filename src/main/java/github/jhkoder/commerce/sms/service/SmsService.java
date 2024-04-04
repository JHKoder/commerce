package github.jhkoder.commerce.sms.service;

import github.jhkoder.commerce.config.WebClientConfig;
import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.sms.service.request.SmsSendRequest;
import github.jhkoder.commerce.sms.service.response.SmsSendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

import static github.jhkoder.commerce.sms.output.SmsSignupOutput.signupText;
import static java.nio.charset.StandardCharsets.UTF_8;

@Service
@RequiredArgsConstructor
public class SmsService {
    private final WebClientConfig client;

    @Value("${sms.platform.apick.api-key}")
    private String apiKey;
    @Value("${sms.platform.apick.uri}")
    private String uri;
    @Value("${sms.from}")
    private String from;

    public SmsSendResponse send(SmsSendRequest request){
        return send(request.to(),request.text());
    }

    public void signupCertSend(String sms, int verificationCode) {
        String signupContext = signupText(verificationCode);
        SmsSendResponse response = send(sms,signupContext);
        if(response.data().error().equals("null")){
            throw new ApiException(ErrorCode.SMS_SEND_FAIL);
        }
    }

    private SmsSendResponse send(String to,String text){
        return client.webClient()
                .post()
                .uri(uri)
                .body(BodyInserters.fromFormData("from",from)
                        .with("to",to)
                        .with("text",text)
                )
                .acceptCharset(UTF_8)
                .header("CL_AUTH_KEY", apiKey)
                .retrieve()
                .bodyToMono(SmsSendResponse.class)
                .block();
    }
}
