package com.example.todo_list_rest_api.task.controller;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.Nullable;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

@RestControllerAdvice
public class ControllerExceptionHandler extends ResponseEntityExceptionHandler {

	@Override
	protected ResponseEntity<Object> handleExceptionInternal(
			Exception ex, @Nullable Object body, HttpHeaders headers, HttpStatus status, WebRequest request) {

		String routeCauseMessage;
		if (!(body instanceof ResponseBodyOnlyMessage)) {
			String reasonPhrase = status.getReasonPhrase();
			routeCauseMessage = ExceptionUtils.getRootCauseMessage(ex);
			if (!"".equals(StringUtils.defaultString(routeCauseMessage))) {
				reasonPhrase += ": " + routeCauseMessage;
			}
			body = new ResponseBodyOnlyMessage(reasonPhrase);
		}
		return new ResponseEntity<>(body, headers, status);
	}
}
