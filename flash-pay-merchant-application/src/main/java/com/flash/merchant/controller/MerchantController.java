package com.flash.merchant.controller;

import com.flash.common.domain.BusinessException;
import com.flash.common.domain.CommonErrorCode;
import com.flash.common.domain.PageVO;
import com.flash.common.util.QRCodeUtil;
import com.flash.common.util.StringUtil;
import com.flash.merchant.api.IMerchantService;
import com.flash.merchant.api.dto.MerchantDto;
import com.flash.merchant.api.dto.StoreDto;
import com.flash.merchant.api.vo.MerchantDetailVo;
import com.flash.merchant.api.vo.MerchantRegistryVo;
import com.flash.merchant.service.ISmsService;
import com.flash.merchant.service.IUploadService;
import com.flash.merchant.utils.SecurityUtil;
import com.flash.transaction.api.ITransactionService;
import com.flash.transaction.api.dto.QRCodeDto;
import io.swagger.annotations.*;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@RestController
@Api(value = "商户平台应用接口", tags = {"Merchant"})
public class MerchantController {
    @Reference
    private IMerchantService iMerchantService;

    @Autowired
    private ISmsService iSmsService;
    @Autowired
    private IUploadService iUploadService;

    @Value("${flash-pay.c2b.subject}")
    private String subject;
    @Value("${flash-pay.c2b.body}")
    private String body;

    @Reference
    private ITransactionService iTransactionService;

    @GetMapping(value = "/my/apps/{appId}/stores/{storeId}/app‐store‐qrcode")
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

    @PostMapping("/my/stores/merchants/page")
    @ApiOperation("分页查询商户下的门店")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "pageNo", value = "页码", required = true, dataType = "int", paramType = "query"),
            @ApiImplicitParam(name = "pageSie", value = "每页记录数", required = true, dataType = "int", paramType = "query")
    })
    public PageVO<StoreDto> queryStoreByPage(@RequestParam Integer pageNo, @RequestParam Integer pageSize) {
        Long merchantId = SecurityUtil.getMerchantId();
        StoreDto storeDto = new StoreDto();
        storeDto.setMerchantId(merchantId);

        return iMerchantService.queryStoreByPage(storeDto, pageNo, pageSize);
    }

    @ApiOperation(value = "资质申请")
    @PostMapping("/my/merchants/save")
    @ApiImplicitParam(value = "资质申请信息", name = "merchantDetailVO", required = true, dataType = "MerchantDetailVO", paramType = "body")
    public void saveMerchant(@RequestBody MerchantDetailVo merchantDetailVo) {
        // 解析请求头中的token得到商户id
        Long merchantId = SecurityUtil.getMerchantId();
        // 资质申请
        iMerchantService.applyMerchant(merchantId, merchantDetailVo);
    }

    @ApiOperation("证件上传")
    @PostMapping("/upload")
    public String uploadImage(@ApiParam(value = "上传的文件", required = true) @RequestParam("file") MultipartFile file) throws IOException {
        return iUploadService.uploadImage(file.getOriginalFilename(), file.getBytes());
    }

    @PostMapping("/merchants/register")
    @ApiOperation("商户注册")
    public MerchantDto registerMerchant(@RequestBody MerchantRegistryVo merchantRegister) throws IOException {
        if (StringUtil.isBlank(merchantRegister.getVerifiykey())
                || StringUtil.isBlank(merchantRegister.getVerifiyCode())) {
            throw new BusinessException(CommonErrorCode.E_100103);
        }

        // 校验验证码
        iSmsService.checkVerifyCode(merchantRegister.getVerifiykey(), merchantRegister.getVerifiyCode());

        // 注册商户
        MerchantDto merchantDto = new MerchantDto();
        merchantDto.setUsername(merchantRegister.getUsername());
        merchantDto.setMobile(merchantRegister.getMobile());
        merchantDto.setPassword(merchantRegister.getPassword());
        return iMerchantService.createMerchant(merchantDto);
    }

    @GetMapping("/sms")
    @ApiOperation("获取短信验证码秘钥")
    @ApiImplicitParam(name = "phone", value = "手机号", required = true, dataType = "String", paramType = "query")
    public String getSmsCode(@RequestParam("phone") String phone) throws IOException {
        return iSmsService.getSmsCode(phone);
    }

    @GetMapping("/my/merchants")
    @ApiOperation("根据租户id查询商户详细信息")
    public MerchantDto queryMerchantTenantId(@RequestParam("tenantId") Long tenantId) {
        return iMerchantService.queryMerchantByTenantId(tenantId);
    }

    @GetMapping("/merchants/{id}")
    @ApiOperation("根据商户id查询商户详细信息")
    public MerchantDto queryMerchantById(@PathVariable("id") Long id) {
        return iMerchantService.queryMerchantById(id);
    }
}
