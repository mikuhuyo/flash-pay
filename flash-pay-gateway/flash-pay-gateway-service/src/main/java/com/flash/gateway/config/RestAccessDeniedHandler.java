package com.flash.gateway.config;

import com.flash.common.domain.RestResponse;
import com.flash.gateway.common.utils.HttpUtil;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuelimin
 */
public class RestAccessDeniedHandler implements AccessDeniedHandler {
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException, ServletException {
        RestResponse<Object> restResponse = new RestResponse<Object>(HttpStatus.FORBIDDEN.value(), "没有权限");
        HttpUtil.writerError(restResponse, response);
    }
}
