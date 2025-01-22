package com.mss.pre.common.response;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * API 에러 반환 모델
 */
@Getter
@Setter
@NoArgsConstructor
public class ApiExceptionResponse {
    String exceptionCode;
    String exceptionMessage;
    HttpStatus httpStatus;
}
