package github.jhkoder.commerce.common.error;

public class ErrorDocument {
    public static ErrorDescriptor errorcode(int errorCode, String descriptor, ErrorDescriptor... descriptors) {
        return new ErrorDescriptor(errorCode,descriptor);
    }
}

