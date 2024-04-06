package github.jhkoder.commerce.sms.service.response;

import com.fasterxml.jackson.annotation.JsonAutoDetect;

import java.util.Objects;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.ANY;

@JsonAutoDetect(getterVisibility = ANY)
public record SmsSendResponse(SmsSendData data ,SmsSendApi api ) {
    @JsonAutoDetect(getterVisibility = ANY)
    public record SmsSendData(String error,String result,String from,String to,String origin_text,int success){
        public boolean isError(){
            try{
                if(this.error == null){
                    return true;
                }else return Objects.equals(this.error, "null");
            }catch (NullPointerException e){
                return true;
            }
        }
    }
    @JsonAutoDetect(getterVisibility = ANY)
    public record SmsSendApi(boolean success,int cost,int ms,int pl_id){}
}
