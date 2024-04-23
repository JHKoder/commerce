package github.jhkoder.commerce.pay.exception;

import github.jhkoder.commerce.exception.ErrorCode;
import lombok.Getter;

@Getter
public class PayException extends RuntimeException{

    private final ErrorCode errorCode;

    public PayException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }
}
