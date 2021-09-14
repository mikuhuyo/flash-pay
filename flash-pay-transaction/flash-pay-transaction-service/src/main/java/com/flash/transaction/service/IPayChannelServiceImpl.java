package com.flash.transaction.service;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.flash.common.cache.Cache;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.util.RedisUtil;
import com.flash.common.util.StringUtil;
import com.flash.transaction.api.IPayChannelService;
import com.flash.transaction.api.dto.PayChannelDto;
import com.flash.transaction.api.dto.PayChannelParamDto;
import com.flash.transaction.api.dto.PlatformChannelDto;
import com.flash.transaction.convert.PayChannelParamConvert;
import com.flash.transaction.convert.PlatformChannelConvert;
import com.flash.transaction.entity.AppPlatformChannel;
import com.flash.transaction.entity.PayChannel;
import com.flash.transaction.entity.PayChannelParam;
import com.flash.transaction.mapper.AppPlatformChannelMapper;
import com.flash.transaction.mapper.PayChannelParamMapper;
import com.flash.transaction.mapper.PlatformChannelMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Service
public class IPayChannelServiceImpl implements IPayChannelService {
    @Autowired
    private PlatformChannelMapper platformChannelMapper;
    @Autowired
    private AppPlatformChannelMapper appPlatformChannelMapper;
    @Autowired
    private PayChannelParamMapper payChannelParamMapper;

    @Resource
    private Cache cache;

    @Override
    public PayChannelParamDto queryPayChannelParamByChannel(String appId, String platformChannel, String payChannel) throws BusinessException {

        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(new LambdaQueryWrapper<AppPlatformChannel>().eq(AppPlatformChannel::getAppId, appId));

        LambdaQueryWrapper<PayChannelParam> eq =
                new LambdaQueryWrapper<PayChannelParam>().eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannel.getId()).eq(PayChannelParam::getPayChannel, payChannel);

        return PayChannelParamConvert.INSTANCE.entity2dto(payChannelParamMapper.selectOne(eq));
    }

    @Override
    public PayChannelParamDto queryParamByApplicationPlatformAndPayChannel(String appId, String platformChannel, String payChannel) throws BusinessException {
        List<PayChannelParamDto> payChannelParamDtos = queryPayChannelParamByApplicationPlatform(appId, platformChannel);

        if (payChannelParamDtos != null) {
            for (PayChannelParamDto payChannelParamDto : payChannelParamDtos) {
                if (payChannelParamDto.getPayChannel().equals(payChannel)) {
                    return payChannelParamDto;
                }
            }
        }
        return null;
    }

    @Override
    public List<PayChannelParamDto> queryPayChannelParamByApplicationPlatform(String appId, String platformChannel) {
        // 构建缓存key
        String s = RedisUtil.keyBuilder(appId, platformChannel);
        // 查询是否存在该缓存
        Boolean exists = cache.exists(s);
        if (exists) {
            // 获取缓存
            String str = cache.get(s);
            // 对象转换
            return JSONObject.parseArray(str, PayChannelParamDto.class);
        }

        // 获取PayChannelParam主键
        Long aLong = selectIdByAppPlatformChannel(appId, platformChannel);

        if (aLong == null || aLong == 0) {
            return null;
        }

        List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(
                new LambdaQueryWrapper<PayChannelParam>().eq(PayChannelParam::getAppPlatformChannelId, aLong)
        );

        if (payChannelParams.size() > 0 && payChannelParams != null) {
            updateCache(appId, platformChannel);
            return PayChannelParamConvert.INSTANCE.entityList2dtoList(payChannelParams);
        }

        return null;
    }

    @Override
    public void savePayChannelParam(PayChannelParamDto payChannelParamDTO) throws BusinessException {

        if (payChannelParamDTO == null || StringUtil.isBlank(payChannelParamDTO.getAppId()) || StringUtil.isBlank(payChannelParamDTO.getPlatformChannelCode()) || StringUtil.isBlank(payChannelParamDTO.getPayChannel())) {
            throw new BusinessException(CommonErrorCode.E_300009);
        }

        Long aLong = selectIdByAppPlatformChannel(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());

        if (aLong == null) {
            throw new BusinessException(CommonErrorCode.E_300010);
        }

        PayChannelParam payChannelParam = payChannelParamMapper.selectOne(
                new QueryWrapper<PayChannelParam>().lambda()
                        .eq(PayChannelParam::getAppPlatformChannelId, aLong)
                        .eq(PayChannelParam::getPayChannel, payChannelParamDTO.getPayChannel())
        );

        if (payChannelParam != null) {
            payChannelParam.setChannelName(payChannelParamDTO.getChannelName());
            payChannelParam.setParam(payChannelParamDTO.getParam());
            payChannelParamMapper.updateById(payChannelParam);
        } else {
            PayChannelParam payChannelParam1 = PayChannelParamConvert.INSTANCE.dto2entity(payChannelParamDTO);
            payChannelParam1.setId(null);
            payChannelParam1.setAppPlatformChannelId(aLong);
            payChannelParamMapper.insert(payChannelParam1);
        }

        updateCache(payChannelParamDTO.getAppId(), payChannelParamDTO.getPlatformChannelCode());
    }

    private void updateCache(String appId, String platformChannelCode) {
        // 得到redis中key(付渠道参数配置列表的key)
        // 格式: SJ_PAY_PARAM:应用id:服务类型code
        // 例如: SJ_PAY_PARAM:ebcecedd303249a696914770e66577af: shanju_c2b
        String redisKey = RedisUtil.keyBuilder(appId, platformChannelCode);
        // 根据key查询redis
        Boolean exists = cache.exists(redisKey);
        if (exists) {
            cache.del(redisKey);
        }

        // 根据应用id和服务类型code查询支付渠道参数
        // 根据应用和服务类型找到它们绑定id
        Long appPlatformChannelId = selectIdByAppPlatformChannel(appId, platformChannelCode);
        if (appPlatformChannelId != null) {
            // 应用和服务类型绑定id查询支付渠道参数记录
            List<PayChannelParam> payChannelParams = payChannelParamMapper.selectList(
                    new LambdaQueryWrapper<PayChannelParam>().eq(PayChannelParam::getAppPlatformChannelId, appPlatformChannelId)
            );

            List<PayChannelParamDto> payChannelParamDtos = PayChannelParamConvert.INSTANCE.entityList2dtoList(payChannelParams);
            // 将payChannelParamDTOS转成json串存入redis
            cache.set(redisKey, JSONObject.toJSONString(payChannelParamDtos).toString());
        }
    }

    /**
     * 根据应用id与服务类型查询应用与服务类型绑定id
     *
     * @param appId               应用id
     * @param platformChannelCode 服务类型
     * @return 绑定id
     */
    private Long selectIdByAppPlatformChannel(String appId, String platformChannelCode) {
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(
                new QueryWrapper<AppPlatformChannel>()
                        .lambda()
                        .eq(AppPlatformChannel::getAppId, appId)
                        .eq(AppPlatformChannel::getPlatformChannel, platformChannelCode));

        if (appPlatformChannel != null) {
            return appPlatformChannel.getId();
        }

        return null;
    }

    @Override
    public List<PayChannelDto> queryPayChannelByPlatformChannel(String platformChannelCode) {
        return platformChannelMapper.selectPayChannelByPlatformChannel(platformChannelCode);
    }

    @Override
    public Integer queryAppBindPlatformChannels(String appId, String platformChannel) throws BusinessException {
        Integer integer = appPlatformChannelMapper.selectCount(
                new QueryWrapper<AppPlatformChannel>().lambda()
                        .eq(AppPlatformChannel::getAppId, appId)
                        .eq(AppPlatformChannel::getPlatformChannel, platformChannel)
        );

        if (integer > 0 || integer != null) {
            return 1;
        }

        return 0;
    }

    @Override
    public void bindPlatformChannelForApp(String appId, String platformChannelCodes) throws BusinessException {
        // 查询应用是否绑定了平台渠道
        AppPlatformChannel appPlatformChannel = appPlatformChannelMapper.selectOne(
                new QueryWrapper<AppPlatformChannel>().lambda()
                        .eq(AppPlatformChannel::getAppId, appId)
                        .eq(AppPlatformChannel::getPlatformChannel, platformChannelCodes)
        );

        // 如果应用未绑定就为应用绑定
        if (appPlatformChannel == null) {
            AppPlatformChannel appPlatformChannel1 = new AppPlatformChannel();
            appPlatformChannel1.setAppId(appId);
            appPlatformChannel1.setPlatformChannel(platformChannelCodes);
            appPlatformChannelMapper.insert(appPlatformChannel1);
        }
    }

    @Override
    public List<PlatformChannelDto> queryPlatformChannel() throws BusinessException {
        return PlatformChannelConvert.INSTANCE.entityList2dtoList(platformChannelMapper.selectList(null));
    }
}
