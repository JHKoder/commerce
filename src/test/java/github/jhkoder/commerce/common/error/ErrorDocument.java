package github.jhkoder.commerce.common.error;

import org.springframework.restdocs.snippet.IgnorableDescriptor;


public class ErrorDocument extends IgnorableDescriptor<ErrorDocument>{
    public static ErrorDescriptor errorcode(int errorCode, String descriptor, ErrorDescriptor... descriptors) {
        return new ErrorDescriptor(errorCode,descriptor);
    }
}

