package com.flash.merchant.api.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 1.8
 */
@Data
@ApiModel(value = "MerchantDto", description = "")
public class MerchantDto implements Serializable {

    private static final long serialVersionUID = 1L;

    @ApiModelProperty(value = "主键")
    private Long id;

    @ApiModelProperty(value = "商户名称")
    private String merchantName;

    @ApiModelProperty(value = "企业编号")
    private String merchantNo;

    @ApiModelProperty(value = "企业地址")
    private String merchantAddress;

    @ApiModelProperty(value = "商户类型")
    private String merchantType;

    @ApiModelProperty(value = "营业执照(企业证明)")
    private String businessLicensesImg;

    @ApiModelProperty(value = "法人身份证正面照片")
    private String idCardFrontImg;

    @ApiModelProperty(value = "法人身份证反面照片")
    private String idCardAfterImg;

    @ApiModelProperty(value = "联系人姓名")
    private String username;

    @ApiModelProperty(value = "用户密码")
    private String password;

    @ApiModelProperty(value = "联系人手机号(关联统一账号)")
    private String mobile;

    @ApiModelProperty(value = "联系人地址")
    private String contactsAddress;

    @ApiModelProperty(value = "审核状态 0-未申请,1-已申请待审核,2-审核通过,3-审核拒绝")
    private String auditStatus;

    @ApiModelProperty(value = "租户ID,关联统一用户")
    private Long tenantId;


}
