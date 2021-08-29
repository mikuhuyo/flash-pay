package com.flash.merchant.api;

import com.flash.common.domain.BusinessException;
import com.flash.merchant.api.dto.AppDto;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface IAppService {
    /**
     * 查询应用是否属于某一个商户
     *
     * @param appId      应用id
     * @param merchantId 商户id
     * @return true 属于, false 不属于
     * @throws BusinessException 自定义异常
     */
    Boolean queryAppInMerchant(String appId, Long merchantId) throws BusinessException;

    /**
     * 根据应用id查询应用
     *
     * @param appId 应用id
     * @return AppDto
     * @throws BusinessException 自定义异常
     */
    AppDto getAppById(String appId) throws BusinessException;

    /**
     * 根据商户id查询
     *
     * @param merchantId 商户id
     * @return List<AppDto>
     * @throws BusinessException 自定义异常
     */
    List<AppDto> queryAppByMerchantId(Long merchantId) throws BusinessException;

    /**
     * 创建应用
     *
     * @param merchantId 商户id
     * @param appDto     应用信息
     * @return AppDto
     * @throws BusinessException 自定义异常
     */
    AppDto createApp(Long merchantId, AppDto appDto) throws BusinessException;
}
