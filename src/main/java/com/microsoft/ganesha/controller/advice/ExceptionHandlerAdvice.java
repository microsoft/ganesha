package com.microsoft.ganesha.controller.advice;

import com.microsoft.ganesha.exception.CustomWebClientException;
import com.microsoft.ganesha.response.ErrorResponse;
import com.microsoft.ganesha.utils.JSONUtil;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.time.LocalDateTime;
import java.util.Optional;

@RestControllerAdvice
public class ExceptionHandlerAdvice extends ResponseEntityExceptionHandler {

    private static final Logger LOGGER = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatusCode status, WebRequest request) {
        String reqStr = JSONUtil.convertObjectToJson(ex.getTarget());

        String correlationId = new JSONObject(reqStr).getJSONObject("searchInputMetaData")
                .get("correlationId").toString();

        LOGGER.error("MethodArgumentNotValidException thrown due to bad request received from with Status code {}", ex.getStatusCode());

        Optional<String> opObj = ex.getBindingResult().getAllErrors().stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .findFirst();

        String errMsg = opObj.isPresent() ? opObj.get() : "";

        ErrorResponse errorResponse = ErrorResponse.builder()
                .timestamp(LocalDateTime.now())
                .errorTitle(HttpStatus.BAD_REQUEST)
                .errorStatus(HttpStatus.BAD_REQUEST.value())
                .errorMessage(errMsg)
                .correlationId(correlationId)
                .build();

        return ResponseEntity.status(errorResponse.getErrorStatus()).body(errorResponse);
    }

    @ExceptionHandler(CustomWebClientException.class)
    public ResponseEntity<Object> handleCustomWebClientException(CustomWebClientException exception) {
        LOGGER.error("CustomWebClientException occurred :: {}", exception.getMessage());
        return new ResponseEntity<>(
                exception.getErrorResponse(),
                HttpStatusCode.valueOf(exception.getErrorResponse().getErrorStatus())
        );
    }
}
