package github.jhkoder.commerce.pay.service;

import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.pay.exception.PayException;
import kr.co.bootpay.Bootpay;
import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;

@Getter
@Configuration
public class BootPayManager {
    private final Bootpay bootpay;
    private String accessToken;

    public BootPayManager(@Value("${pay.platform.boot-pay.application-id.rest-api}") String restapiKey,
                          @Value("${pay.platform.boot-pay.private-key}") String privateKey) {
        this.bootpay = new Bootpay(restapiKey, privateKey);
        this.accessToken = "";
//        bootPayConnect();
//        scheduleBeanRefresh();
    }

//    @Scheduled(fixedDelay = 1560000) // 26분
//    private void refreshBootPayBean() {
//        bootPayConnect();
//    }

    public void bootPayConnect() {
        try {
            HashMap<String, Object> res = bootpay.getAccessToken();
            if (res.get("error_code") == null) {
                this.accessToken = res.get("access_token").toString();
                return;
            }
        } catch (Exception ignored) {
        }
        throw new PayException(ErrorCode.PAY_ACCESS_TOKEN_CONNECT_FAIL);
    }


//    private void scheduleBeanRefresh() {
//        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
//        scheduler.scheduleAtFixedRate(this::refreshBootPayBean, 0, 1560, TimeUnit.SECONDS);
//    }

}
