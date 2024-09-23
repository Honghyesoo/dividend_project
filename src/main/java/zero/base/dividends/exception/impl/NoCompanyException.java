package zero.base.dividends.exception.impl;

import org.springframework.http.HttpStatus;
import zero.base.dividends.exception.AbstractException;

public class NoCompanyException extends AbstractException {
    @Override
    public int getStatusCode() {
        return HttpStatus.BAD_REQUEST.value(); //400에러
    }

    @Override
    public String getMessage() {
        return "존재하지 않는 회사명 입니다.";
    }
}
