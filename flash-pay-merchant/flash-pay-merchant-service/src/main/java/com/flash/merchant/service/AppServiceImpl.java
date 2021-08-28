package com.flash.merchant.service;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.util.RandomUuidUtil;
import com.flash.merchant.api.IAppService;
import com.flash.merchant.api.dto.AppDto;
import com.flash.merchant.convert.AppConvert;
import com.flash.merchant.entity.App;
import com.flash.merchant.entity.Merchant;
import com.flash.merchant.mapper.AppMapper;
import com.flash.merchant.mapper.MerchantMapper;
import org.apache.dubbo.config.annotation.Service;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Service
public class AppServiceImpl implements IAppService {
    @Autowired
    private AppMapper appMapper;
    @Autowired
    private MerchantMapper merchantMapper;

    @Override
    public AppDto getAppById(String appId) throws BusinessException {
        return AppConvert.INSTANCE.entity2dto(appMapper.selectOne(new LambdaQueryWrapper<App>().eq(App::getAppId, appId)));
    }

    @Override
    public List<AppDto> queryAppByMerchantId(Long merchantId) throws BusinessException {
        LambdaQueryWrapper<App> eq = new LambdaQueryWrapper<App>().eq(App::getMerchantId, merchantId);
        return AppConvert.INSTANCE.entityList2dtoList(appMapper.selectList(eq));
    }

    @Override
    public AppDto createApp(Long merchantId, AppDto appDto) throws BusinessException {
        // 查询商户是否存在以及商户是否审核通过.
        Merchant merchant = merchantMapper.selectById(merchantId);
        if (merchant == null) {
            throw new BusinessException(CommonErrorCode.E_200002);
        }
        if (!"2".equals(merchant.getAuditStatus())) {
            throw new BusinessException(CommonErrorCode.E_200003);
        }

        // 检测应用名称是否重复
        if (!checkExistsAppName(appDto.getAppName())) {
            throw new BusinessException(CommonErrorCode.E_200004);
        }

        // 保存应用信息
        App app = AppConvert.INSTANCE.dto2entity(appDto);
        app.setAppId(RandomUuidUtil.getUUID());
        app.setMerchantId(merchantId);
        appMapper.insert(app);

        return AppConvert.INSTANCE.entity2dto(app);
    }

    /**
     * 查询应用名是否重复
     *
     * @param appName 应用名称
     * @return true-存在, false-不存在
     */
    private Boolean checkExistsAppName(String appName) {
        LambdaQueryWrapper<App> eq = new LambdaQueryWrapper<App>().eq(App::getAppName, appName);
        return appMapper.selectCount(eq) == 0;
    }
}
