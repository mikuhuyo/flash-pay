package com.flash.merchant.service;

import com.flash.common.domain.BusinessException;

import java.io.IOException;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface IUploadService {
    /**
     * 上传图片
     *
     * @param fileName 图片文件名称
     * @param bytes    图片文件二进制数据
     * @return 图片url地址
     * @throws BusinessException 自定义异常
     * @throws IOException       io异常
     */
    String uploadImage(String fileName, byte[] bytes) throws BusinessException, IOException;
}
