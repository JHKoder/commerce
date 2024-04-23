package github.jhkoder.commerce.pay.service;

import kr.co.bootpay.model.request.Cancel;

public class PayCancel extends Cancel {

    public static Cancel of(String receiptId,String name,String message){
        Cancel cancel = new Cancel();
        cancel.receiptId = receiptId;
        cancel.cancelUsername =name;
        cancel.cancelMessage =message;
        return cancel;
    }
}
