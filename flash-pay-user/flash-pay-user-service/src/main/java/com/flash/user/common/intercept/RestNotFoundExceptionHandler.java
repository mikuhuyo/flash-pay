package com.flash.user.common.intercept;

import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.RestResponse;
import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

/**
 * 处理404
 *
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@RestController
public class RestNotFoundExceptionHandler implements ErrorController {
    private static final String ERROR_PATH = "/error";

    @RequestMapping(value = ERROR_PATH)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public RestResponse<CommonErrorCode> handleError() {
        CommonErrorCode u = CommonErrorCode.E_100116;
        return new RestResponse<CommonErrorCode>(u.getCode(), u.getDesc());
    }

    @Override
    public String getErrorPath() {
        return ERROR_PATH;
    }
}

