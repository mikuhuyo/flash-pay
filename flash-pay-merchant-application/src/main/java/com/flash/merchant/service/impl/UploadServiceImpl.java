package com.flash.merchant.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.flash.common.domain.BusinessException;
import com.flash.merchant.service.IUploadService;
import lombok.extern.slf4j.Slf4j;
import okhttp3.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;
import java.util.Objects;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Slf4j
@Service
public class UploadServiceImpl implements IUploadService {
    @Value("${minio.url}")
    private String url;
    @Value("${minio.id}")
    private String id;
    @Value("${minio.ak}")
    private String ak;
    @Value("${minio.sk}")
    private String sk;

    @Override
    public String uploadImage(String fileName, byte[] bytes) throws BusinessException, IOException {
        OkHttpClient client = new OkHttpClient().newBuilder().build();

        RequestBody body = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("file", fileName, RequestBody.create(MediaType.parse("application/octet-stream"), bytes))
                .build();

        Request request = new Request.Builder()
                .url(url + "/image/" + fileName)
                .method("POST", body)
                .addHeader("AppId", id)
                .addHeader("AccessKey", ak)
                .addHeader("SecretKey", sk)
                .build();
        String string = Objects.requireNonNull(client.newCall(request).execute().body()).string();

        log.info("调用文件服务-获取响应信息: {}", string);

        Map<String, Object> map = JSONObject.parseObject(string, new TypeReference<Map<String, Object>>() {
        });

        if (!(Boolean) map.get("flag")) {
            throw new BusinessException("文件上传失败, 请重新上传");
        }

        return map.get("data").toString();
    }
}
