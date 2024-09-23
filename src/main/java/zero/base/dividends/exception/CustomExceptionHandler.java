package zero.base.dividends.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice //전역 예외 처리를 담당하는 어노테이션
public class CustomExceptionHandler {
    //
    @ExceptionHandler(AbstractException.class)
    protected ResponseEntity<ErrorResponse> HandleCustomException(AbstractException e) {
        log.error("HandleCustomException: {}", e.getMessage(), e);  // 로깅 추가

        ErrorResponse errorResponse = ErrorResponse.builder()
                .code(e.getStatusCode())
                .message(e.getMessage())
                .build();

         return new ResponseEntity<>(errorResponse, HttpStatus.resolve(e.getStatusCode()));
    }
}
