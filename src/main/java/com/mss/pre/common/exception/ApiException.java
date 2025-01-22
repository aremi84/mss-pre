package com.mss.pre.common.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * 사용자 정의 에러 모델
 */
@Getter
@Setter
public class ApiException extends RuntimeException {
    private String message;
    private HttpStatus httpStatus;

    public ApiException(String message) {
        setMessage(message);
    }

    public ApiException(String message, HttpStatus httpStatus) {
        setMessage(message);
        setHttpStatus(httpStatus);
    }
}
