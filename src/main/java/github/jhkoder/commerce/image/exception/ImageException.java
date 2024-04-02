package github.jhkoder.commerce.image.exception;

import github.jhkoder.commerce.exception.ErrorCode;

public class ImageException extends RuntimeException {

    private final ErrorCode errorCode;

    public ImageException(ErrorCode errorCode) {
        super(errorCode.getMessage());
        this.errorCode = errorCode;
    }

}