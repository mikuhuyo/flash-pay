package com.flash.user.common.intercept;

import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.ErrorCode;
import com.flash.common.domain.RestResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@RestControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger LOGGER = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public RestResponse<CommonErrorCode> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        LOGGER.info(e.getMessage(), e);

        CommonErrorCode u = CommonErrorCode.E_100101;
        return new RestResponse<CommonErrorCode>(u.getCode(), e.getBindingResult().getAllErrors().get(0).getDefaultMessage());
    }

    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public RestResponse<CommonErrorCode> handlerHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        LOGGER.info(e.getMessage(), e);

        CommonErrorCode u = CommonErrorCode.E_100117;
        return new RestResponse<CommonErrorCode>(u.getCode(), u.getDesc());
    }

    @ResponseStatus(HttpStatus.UNSUPPORTED_MEDIA_TYPE)
    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    public RestResponse<CommonErrorCode> handlerHttpMediaTypeNotSupportedException(HttpMediaTypeNotSupportedException e) {
        LOGGER.info(e.getMessage(), e);

        CommonErrorCode u = CommonErrorCode.E_100118;
        return new RestResponse<CommonErrorCode>(u.getCode(), u.getDesc());
    }

    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    @ExceptionHandler(value = Exception.class)
    public RestResponse<CommonErrorCode> processException(Exception e) {
        LOGGER.info(e.getMessage(), e);

        if (e instanceof BusinessException) {
            BusinessException businessException = (BusinessException) e;
            if (businessException.getErrorCode() != null) {
                ErrorCode errorCode = businessException.getErrorCode();
                return new RestResponse<CommonErrorCode>(errorCode.getCode(), errorCode.getDesc());
            }
        }

        CommonErrorCode u = CommonErrorCode.UNKNOWN;
        return new RestResponse<CommonErrorCode>(u.getCode(), u.getDesc());
    }
}
