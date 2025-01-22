package com.mss.pre.common.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

/**
 * API 결과 반환 모델
 * @param <T>
 */
@Getter
public class ApiResponse<T> {
    T data;
    @Setter
    HttpStatus httpStatus;

    public ApiResponse(@JsonProperty("data") T data) {
        this.data = data;
    }

    public ApiResponse() {

    }

    @JsonProperty("data")
    public void setData(T data) {
        this.data = data;
    }
}
