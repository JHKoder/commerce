package github.jhkoder.commerce.sms.service;

import github.jhkoder.commerce.config.WebClientConfig;
import github.jhkoder.commerce.sms.service.request.SmsSendRequest;
import github.jhkoder.commerce.sms.service.response.SmsSendResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;

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
        return client.webClient()
                .post()
                .uri(uri)
                .body(BodyInserters.fromFormData("from",from)
                        .with("to",request.to())
                        .with("text",request.text())
                )
                .acceptCharset(UTF_8)
                .header("CL_AUTH_KEY", apiKey)
                .retrieve()
                .bodyToMono(SmsSendResponse.class)
                .block();
    }
}
