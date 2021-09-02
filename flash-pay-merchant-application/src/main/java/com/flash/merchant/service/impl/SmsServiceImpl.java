package com.flash.merchant.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.flash.common.domain.BusinessException;
import com.flash.merchant.service.ISmsService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Service
public class SmsServiceImpl implements ISmsService {
    @Value("${sms.name}")
    private String name;
    @Value("${sms.url}")
    private String url;
    @Value("${sms.effectiveTime}")
    private String effectiveTime;

    @Override
    public void checkVerifyCode(String verifyKey, String verifyCode) throws BusinessException, IOException {
        String smsUrl = url + "/verify?" + "name=" + name + "&verificationCode=" + verifyCode + "&verificationKey=" + verifyKey;

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("text/plain");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url(smsUrl)
                .method("POST", body)
                .build();

        String string = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        log.info("调用腾讯云短信服务-获取响应: {}", string);

        Map<String, Object> map = JSONObject.parseObject(string, new TypeReference<Map<String, Object>>() {
        });

        if (!(Boolean) map.get("result")) {
            throw new BusinessException("验证码错误, 请重新输入.");
        }
    }

    @Override
    public String getSmsCode(String mobile) throws BusinessException, IOException {
        String smsUrl = url + "/generate?" + "effectiveTime=" + effectiveTime + "&name=" + name;

        Map<String, Object> load = new HashMap<>();
        load.put("mobile", mobile);

        OkHttpClient client = new OkHttpClient().newBuilder().build();
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, JSONObject.toJSONString(load));
        Request request = new Request.Builder()
                .url(smsUrl)
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .build();

        String jsonString = Objects.requireNonNull(client.newCall(request).execute().body()).string();
        log.info("调用腾讯云短信服务-获取响应: {}", jsonString);
        Map<String, Object> map = JSONObject.parseObject(jsonString, new TypeReference<Map<String, Object>>() {
        });

        if (map.get("result") == null) {
            throw new BusinessException("验证码获取失败, 请重新获取.");
        }

        Map<String, Object> result = JSONObject.parseObject(JSONObject.toJSONString(map.get("result")), new TypeReference<Map<String, Object>>() {
        });

        return result.get("key").toString();
    }
}
