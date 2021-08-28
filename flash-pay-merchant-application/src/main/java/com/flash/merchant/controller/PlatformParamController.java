package com.flash.merchant.controller;

import com.flash.merchant.utils.SecurityUtil;
import com.flash.transaction.api.IPayChannelService;
import com.flash.transaction.api.dto.PayChannelDto;
import com.flash.transaction.api.dto.PayChannelParamDto;
import com.flash.transaction.api.dto.PlatformChannelDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@RestController
@Api(value = "商户应用-渠道和支付参数相关", tags = {"PlatformPara"})
public class PlatformParamController {
    @Reference
    private IPayChannelService iPayChannelService;

    @GetMapping("/my/pay-channel-params/apps/{appId}/platform-channels/{platformChannel}")
    @ApiOperation("获取指定应用指定服务类型下所包含的原始支付渠道参数列表")
    public List<PayChannelParamDto> queryChannelParam(@PathVariable("appId") String appId, @PathVariable("platformChannel") String platformChannel) {
        return iPayChannelService.queryPayChannelParamByApplicationPlatform(appId, platformChannel);
    }

    @GetMapping("/my/pay-channel-params/apps/{appId}/platform-channels/{platformChannel}/pay-channels/{payChannel}")
    @ApiOperation("获取指定应用指定服务类型下所包含的某个原始支付参数")
    public PayChannelParamDto queryPayChannelParam(@PathVariable("appId") String appId, @PathVariable("platformChannel") String platformChannel, @PathVariable("payChannel") String payChannel) {
        return iPayChannelService.queryParamByApplicationPlatformAndPayChannel(appId, platformChannel, payChannel);
    }

    @RequestMapping(value = "/my/pay-channel-params", method = {RequestMethod.POST, RequestMethod.PUT})
    @ApiOperation("保存支付渠道参数")
    public void createPayChannelParam(@RequestBody PayChannelParamDto payChannelParamDto) {
        Long merchantId = SecurityUtil.getMerchantId();
        payChannelParamDto.setMerchantId(merchantId);
        iPayChannelService.savePayChannelParam(payChannelParamDto);
    }

    @GetMapping("/my/pay-channels/platform-channel/{platformChannelCode}")
    @ApiOperation("根据平台服务类型获取支付渠道列表")
    public List<PayChannelDto> queryPayChannelByPlatformChannel(@PathVariable("platformChannelCode") String code) {
        return iPayChannelService.queryPayChannelByPlatformChannel(code);
    }

    @GetMapping("/my/merchants/apps/platform-channels")
    @ApiOperation("查询应用是否绑定服务")
    public Integer queryAppBindPlatformChannel(@RequestParam("appId") String appId, @RequestParam("platformChannel") String platformChannel) {
        return iPayChannelService.queryAppBindPlatformChannels(appId, platformChannel);
    }

    @PostMapping("/my/apps/{appId}/platform-channels")
    @ApiOperation(value = "绑定服务类型")
    public void bindPlatformForApp(@PathVariable("appId") String appId, @RequestParam("platformChannelCodes") String platformChannelCodes) {
        iPayChannelService.bindPlatformChannelForApp(appId, platformChannelCodes);
    }

    @GetMapping("/my/platform-channels")
    @ApiOperation(value = "获取平台服务类型")
    public List<PlatformChannelDto> queryPlatformChanel() {
        return iPayChannelService.queryPlatformChannel();
    }
}
