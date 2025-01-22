package com.mss.pre.common.response;

import com.mss.pre.common.exception.ApiException;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

/**
 * API 결과를 공통 규격으로 변환하여 반환한다.
 * - 정상: {@link ApiResponse}
 * - 에러: {@link ApiExceptionResponse}
 */
@RestControllerAdvice
public class ApiResponseBodyAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType,
			MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType,
			ServerHttpRequest request, ServerHttpResponse response
	) {
		ApiResponse<Object> res = new ApiResponse<>(body);
		res.setHttpStatus(HttpStatus.OK);
		return getResult(body, response, res);
	}

	protected Object getResult(Object body, ServerHttpResponse response, ApiResponse<Object> res) {
		try {
			if (body instanceof ApiException) {
				ApiException exception = (ApiException) body;
				ApiExceptionResponse exceptionResponse = new ApiExceptionResponse();
				exceptionResponse.setExceptionMessage(exception.getMessage());
				exceptionResponse.setExceptionCode(String.valueOf(exception.getHttpStatus().value()));
				exceptionResponse.setHttpStatus(exception.getHttpStatus());
				response.setStatusCode(exception.getHttpStatus());
				return exceptionResponse;

			} else if (body instanceof Exception) {
				Exception exception = (Exception) body;
				ApiExceptionResponse exceptionResponse = new ApiExceptionResponse();
				exceptionResponse.setExceptionMessage(exception.getMessage());
				exceptionResponse.setHttpStatus(HttpStatus.BAD_GATEWAY);
				response.setStatusCode(HttpStatus.BAD_GATEWAY);
				return exceptionResponse;
			}

		} catch (Exception e) {
			ApiExceptionResponse exceptionResponse = new ApiExceptionResponse();
			exceptionResponse.setExceptionMessage(e.getMessage());
			exceptionResponse.setHttpStatus(HttpStatus.BAD_GATEWAY);
			return exceptionResponse;
		}
		return res;
	}

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return true;
	}
}
