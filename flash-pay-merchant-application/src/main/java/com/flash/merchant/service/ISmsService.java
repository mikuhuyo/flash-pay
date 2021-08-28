package com.flash.merchant.service;

import com.flash.common.domain.BusinessException;

import java.io.IOException;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface ISmsService {

    /**
     * 验证码校验
     *
     * @param verifyKey  秘钥
     * @param verifyCode 验证码
     * @throws BusinessException 自定义异常
     */
    void checkVerifyCode(String verifyKey, String verifyCode) throws BusinessException, IOException;

    /**
     * 获取短信验证码秘钥
     *
     * @param mobile 手机号
     * @return 秘钥
     * @throws BusinessException 自定义业务异常
     */
    String getSmsCode(String mobile) throws BusinessException, IOException;
}
