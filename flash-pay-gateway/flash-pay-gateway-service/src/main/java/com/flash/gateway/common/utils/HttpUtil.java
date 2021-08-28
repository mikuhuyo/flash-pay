package com.flash.gateway.common.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.flash.common.domain.RestResponse;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public class HttpUtil {
    public static void writerError(RestResponse restResponse, HttpServletResponse response) throws IOException {
        response.setContentType("application/json,charset=utf-8");
        response.setStatus(restResponse.getCode());
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.writeValue(response.getOutputStream(), restResponse);
    }
}
