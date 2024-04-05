package github.jhkoder.commerce.sms.service;

import github.jhkoder.commerce.sms.service.request.SmsSendRequest;
import github.jhkoder.commerce.sms.service.response.SmsSendResponse;

public class SmsFakeService implements SmsService {
    @Override
    public SmsSendResponse send(SmsSendRequest request) {
        return send(request.to(), request.text());
    }

    private SmsSendResponse send(String to, String text) {
        return new SmsSendResponse(
                new SmsSendResponse.SmsSendData("null", "848563e6-534c-488c-1c65-234sd2141", "821012345678", "821012341234", "API Test message", 1),
                new SmsSendResponse.SmsSendApi(true, 6, 884, 10180981)
        );
    }

    public void signupCertSend(String sms, int verificationCode) {
    }
}
