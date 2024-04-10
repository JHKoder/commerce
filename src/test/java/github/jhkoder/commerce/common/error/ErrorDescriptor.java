package github.jhkoder.commerce.common.error;

import github.jhkoder.commerce.exception.ErrorCode;

public record ErrorDescriptor(int code, String description)  {
    public static ErrorDescriptor of(ErrorCode errorCode){
        return new ErrorDescriptor(errorCode.getStatus(),errorCode.getMessage());
    }
}
