package github.jhkoder.commerce.sms.service.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import lombok.Data;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@Data
@JsonAutoDetect(getterVisibility = ANY)
public class SmsSendResponse {
    private SmsSendData data;
    private SmsSendApi api;
    public record SmsSendData(String error,String result,String from,String to,String origin_text,int success){}

    public record SmsSendApi(boolean success,int cost,int ms,int pl_id){}
}