package github.jhkoder.commerce.pay.service;

import github.jhkoder.commerce.exception.ApiException;
import github.jhkoder.commerce.exception.ErrorCode;
import github.jhkoder.commerce.pay.service.request.PayCancelRequest;
import github.jhkoder.commerce.user.domain.User;
import github.jhkoder.commerce.user.repository.UserRepository;
import kr.co.bootpay.Bootpay;
import kr.co.bootpay.model.request.Cancel;
import kr.co.bootpay.model.request.UserToken;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashMap;

@Service
@RequiredArgsConstructor
public class PayService {
    private final BootPayManager manager;
    private final UserRepository userRepository;

    //결제 서버 승인
    public void pay(){
//        HashMap<String, Object> res = bootpay.confirm(receiptId);
    }
    // 결제 취소
    public void PayCancel(String receiptId,String  username, PayCancelRequest request){
        Cancel cancel = PayCancel.of(receiptId,
                findByUsername(username).getUserName(),
                request.cancelMessage());

        try {
            HashMap<String, Object> res = manager.getBootpay().receiptCancel(cancel);
            if(res.get("error_code") != null) {
                throw new ApiException(ErrorCode.PAY_CANCEL_FAIL);
            }
        } catch (Exception ignored) {
            throw new ApiException(ErrorCode.PAY_CANCEL_FAIL);
        }
    }
    // 단건 조회 결제 검증
    public void receipt(String receiptId){
        // 클라이언트에서 통지된  결제 정보는 변조가 쉬워 rest api 로 결제 단건 조회를 통해 교차 검증
        // status , price 교차 검증
        receiptId = "62876963d01c7e00209b6028";
        try {
            HashMap<String, Object> res = manager.getBootpay().getReceipt(receiptId);
            if(res.get("error_code") != null) {
                throw new ApiException(ErrorCode.PAY_RECEIPT_FAIL);
            }
        } catch (Exception ignored) {
            throw new ApiException(ErrorCode.PAY_RECEIPT_FAIL);
        }
    }


    // 구매자 토큰 발급 받기

    // 서버로 통지 받기

    //사용자 토큰 받기
    public String bootPayUserToken(String username){
        User user = findByUsername(username);
        UserToken token =new UserToken();
        token.userId = "12";
        try {
            HashMap<String, Object> res = manager.getBootpay().getUserToken(token);
            if(res.get("error_code") == null) { //success
                System.out.println("getUserToken success: " + res);
            } else {
                System.out.println("getUserToken false: " + res);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    private User findByUsername(String username){
        return userRepository.findByUserId(username)
                .orElseThrow(() -> new ApiException(ErrorCode.USER_NOT_FOUND));
    }
}
