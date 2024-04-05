package github.jhkoder.commerce.sms.service;

import github.jhkoder.commerce.sms.service.request.SmsSendRequest;
import github.jhkoder.commerce.sms.service.response.SmsSendResponse;

public interface SmsService {
    SmsSendResponse send(SmsSendRequest request);
    void signupCertSend(String sms, int verificationCode);
}
