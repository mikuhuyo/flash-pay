package com.flash.merchant.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;
import java.io.Serializable;
import lombok.Data;
import com.baomidou.mybatisplus.annotation.TableName;

/**
 * @author yuelimin
 * @version 1.0.0
 * @since 11
 */
@Data
@TableName("merchant")
public class Merchant implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键
     */
    @TableId(value = "ID", type = IdType.ID_WORKER)
    private Long id;

    /**
     * 商户名称
     */
    @TableField("MERCHANT_NAME")
    private String merchantName;

    /**
     * 企业编号
     */
    @TableField("MERCHANT_NO")
    private String merchantNo;

    /**
     * 企业地址
     */
    @TableField("MERCHANT_ADDRESS")
    private String merchantAddress;

    /**
     * 商户类型
     */
    @TableField("MERCHANT_TYPE")
    private String merchantType;

    /**
     * 营业执照(企业证明)
     */
    @TableField("BUSINESS_LICENSES_IMG")
    private String businessLicensesImg;

    /**
     * 法人身份证正面照片
     */
    @TableField("ID_CARD_FRONT_IMG")
    private String idCardFrontImg;

    /**
     * 法人身份证反面照片
     */
    @TableField("ID_CARD_AFTER_IMG")
    private String idCardAfterImg;

    /**
     * 联系人姓名
     */
    @TableField("USERNAME")
    private String username;

    /**
     * 联系人手机号(关联统一账号)
     */
    @TableField("MOBILE")
    private String mobile;

    /**
     * 联系人地址
     */
    @TableField("CONTACTS_ADDRESS")
    private String contactsAddress;

    /**
     * 审核状态 0-未申请,1-已申请待审核,2-审核通过,3-审核拒绝
     */
    @TableField("AUDIT_STATUS")
    private String auditStatus;

    /**
     * 租户ID,关联统一用户
     */
    @TableField("TENANT_ID")
    private Long tenantId;


}
