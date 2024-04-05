package github.jhkoder.commerce.common.error;

import github.jhkoder.commerce.exception.ErrorCode;

public class ErrorDocument {
    public static ErrorDescriptor errorcode(int errorCode, String descriptor, ErrorDescriptor... descriptors) {
        return new ErrorDescriptor(errorCode,descriptor);
    }
    public static ErrorDescriptor errorcode(ErrorCode errorCode, ErrorDescriptor... descriptors) {
        return new ErrorDescriptor(errorCode.getStatus(),errorCode.getMessage());
    }
}

