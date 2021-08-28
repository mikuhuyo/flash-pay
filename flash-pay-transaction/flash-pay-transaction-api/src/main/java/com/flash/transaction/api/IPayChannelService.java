package com.flash.transaction.api;

import com.flash.common.domain.BusinessException;
import com.flash.transaction.api.dto.PayChannelDto;
import com.flash.transaction.api.dto.PayChannelParamDto;
import com.flash.transaction.api.dto.PlatformChannelDto;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
public interface IPayChannelService {
    /**
     * 获取指定应用指定服务类型下所包含的原始支付渠道参数列表
     *
     * @param appId           应用ID
     * @param platformChannel 服务类型
     * @return
     */
    List<PayChannelParamDto> queryPayChannelParamByApplicationPlatform(String appId, String platformChannel) throws BusinessException;

    /**
     * 获取指定应用指定服务类型下所包含的某个原始支付参数
     *
     * @param appId           应用ID
     * @param platformChannel 服务类型
     * @param payChannel      支付类型
     * @return
     */
    PayChannelParamDto queryParamByApplicationPlatformAndPayChannel(String appId, String platformChannel, String payChannel) throws BusinessException;

    /**
     * 保存支付渠道参数
     *
     * @param payChannelParamDto 商户原始支付渠道参数
     * @throws BusinessException
     */
    void savePayChannelParam(PayChannelParamDto payChannelParamDto) throws BusinessException;

    /**
     * 根据平台服务类型获取支付渠道列表
     *
     * @param platformChannelCode 平台服务类型
     * @return 支付渠道列表
     * @throws BusinessException
     */
    List<PayChannelDto> queryPayChannelByPlatformChannel(String platformChannelCode) throws BusinessException;

    /**
     * 查询应用是否绑定服务类型
     *
     * @param appId           应用id
     * @param platformChannel 服务类型
     * @return 0-未绑定 1-绑定
     * @throws BusinessException
     */
    Integer queryAppBindPlatformChannels(String appId, String platformChannel) throws BusinessException;

    /**
     * 为应用绑定服务类型
     *
     * @param appId                应用id
     * @param platformChannelCodes 服务编码
     * @throws BusinessException 自定义异常
     */
    void bindPlatformChannelForApp(String appId, String platformChannelCodes) throws BusinessException;

    /**
     * 查询平台服务类型
     *
     * @return List<PlatformChannelDto>
     * @throws BusinessException 自定义异常
     */
    List<PlatformChannelDto> queryPlatformChannel() throws BusinessException;
}
