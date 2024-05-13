package github.jhkoder.commerce.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {


    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<ErrorResponse> handlerServerException(Exception e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INTERNAL_SERVER_ERROR);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }


    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.of(e.getStatusCode().value(),e.getMessage());
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }


    @ExceptionHandler(value = ApiException.class)
    public ResponseEntity<ErrorResponse> handlerApiException(ApiException e) {
        ErrorResponse errorResponse = ErrorResponse.of(e.getErrorCode());;
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<ErrorResponse> handlerIllegalArgumentExceptionException(IllegalArgumentException e) {
        log.error(e.getMessage(), e);
        ErrorResponse errorResponse = ErrorResponse.of(ErrorCode.INVALID_VALUE);
        return ResponseEntity.status(errorResponse.getStatus()).body(errorResponse);
    }
}
