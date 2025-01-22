package com.mss.pre.common.exception;

import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

@RestControllerAdvice
public class ApiExceptionHandler {
    @ExceptionHandler(ApiException.class)
    public ApiException handleApiException(ApiException ex) {
        return new ApiException(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ApiException handleMethodArgumentNotValid(MethodArgumentNotValidException ex) {
        BindingResult bindingResult = ex.getBindingResult();
        StringBuilder errorMessage = new StringBuilder("Validation failed: ");
        bindingResult.getFieldErrors().forEach(fieldError -> errorMessage.append(fieldError.getField())
                .append(" (")
                .append(fieldError.getRejectedValue())
                .append(") - ")
                .append(fieldError.getDefaultMessage())
                .append(", "));
        return new ApiException(errorMessage.toString(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({
            NoHandlerFoundException.class,
            HttpRequestMethodNotSupportedException.class,
            MissingServletRequestParameterException.class,
            HttpRequestMethodNotSupportedException.class
    })
    protected ApiException handleNotFoundException(Exception ex) {
        return new ApiException(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(Exception.class)
    public ApiException handleTypeMismatchException(Exception ex) {
        return new ApiException(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
