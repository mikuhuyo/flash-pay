package com.flash.merchant.controller;

import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.util.QRCodeUtil;
import com.flash.merchant.api.IAppService;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.AppDto;
import com.flash.merchant.api.dto.MerchantDto;
import com.flash.merchant.utils.SecurityUtil;
import com.flash.transaction.api.ITransactionService;
import com.flash.transaction.api.dto.QRCodeDto;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@RestController
@Api(value = "商户平台-应用管理", tags = {"App"})
public class AppController {
    @Reference
    private IAppService iAppService;

    @Value("${flash-pay.c2b.subject}")
    private String subject;
    @Value("${flash-pay.c2b.body}")
    private String body;

    @Reference
    private IMerchantService iMerchantService;
    @Reference
    private ITransactionService iTransactionService;

    @GetMapping(value = "/my/apps/{appId}/stores/{storeId}/app-store-qrcode")
    @ApiOperation("生成商户应用门店二维码")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "商户应用id", required = true, dataType = "String", paramType = "path"),
            @ApiImplicitParam(name = "storeId", value = "商户门店id", required = true, dataType = "String", paramType = "path"),
    })
    public String createCScanBStoreQRCode(@PathVariable("appId") String appId, @PathVariable("storeId") Long storeId) {
        Long merchantId = SecurityUtil.getMerchantId();
        QRCodeDto qrCodeDto = new QRCodeDto();
        qrCodeDto.setMerchantId(merchantId);
        qrCodeDto.setAppId(appId);
        qrCodeDto.setStoreId(storeId);

        MerchantDto merchantDto = iMerchantService.queryMerchantById(merchantId);

        qrCodeDto.setSubject(String.format(subject, merchantDto.getMerchantName()));
        qrCodeDto.setBody(String.format(body, merchantDto.getMerchantName()));

        String code = iTransactionService.createStoreQRCode(qrCodeDto);

        try {
            return QRCodeUtil.createQRCode(code, 200, 200);
        } catch (Exception e) {
            throw new BusinessException(CommonErrorCode.E_200007);
        }
    }

    @GetMapping(value = "/my/apps/{appId}")
    @ApiOperation("根据app-id获取应用的详细信息")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "appId", value = "商户应用id", required = true, dataType = "String", paramType = "path")
    })
    public AppDto getApp(@PathVariable String appId) {
        return iAppService.getAppById(appId);
    }

    @GetMapping(value = "/my/apps")
    @ApiOperation("查询商户下的应用列表")
    public List<AppDto> queryMyApps() {
        Long merchantId = SecurityUtil.getMerchantId();
        return iAppService.queryAppByMerchantId(merchantId);
    }

    @PostMapping(value = "/my/apps")
    @ApiOperation("商户创建应用")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "app", value = "应用信息", required = true, dataType = "AppDTO", paramType = "body")
    })
    public AppDto createApp(@RequestBody AppDto app) {
        Long merchantId = SecurityUtil.getMerchantId();
        return iAppService.createApp(merchantId, app);
    }
}
